from fastapi import FastAPI
import feedparser
import time
import threading
import calendar

# ------------------ USER AGENT (IMPORTANT) ------------------

feedparser.USER_AGENT = (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
    "AppleWebKit/537.36 (KHTML, like Gecko) "
    "Chrome/120.0 Safari/537.36"
)

# ------------------ RSS FEEDS ------------------

RSS_FEEDS = {
    "bbc": "https://feeds.bbci.co.uk/news/world/rss.xml",
    "hindu": "https://www.thehindu.com/news/feeder/default.rss",
    "indiandefense": "https://www.indiandefensenews.in/feeds/posts/default"
}

SOURCE_LIMITS = {
    "bbc": 20,
    "hindu": 10,
    "indiandefense": 10
}

# Priority (lower = shown first)
SOURCE_PRIORITY = {
    "bbc": 0,
    "indiandefense": 1,
    "hindu": 2
}

# ------------------ SOURCE-WISE AGE LIMITS ------------------

SOURCE_MAX_AGE = {
    "bbc": 24 * 60 * 60,                # 1 day
    "hindu": 24 * 60 * 60,              # 1 day
    "indiandefense": 5 * 24 * 60 * 60   # 🔥 5 days
}

REFRESH_INTERVAL = 8 * 60 * 60  # refresh every 8 hours

# ------------------ STORAGE ------------------

DAILY_DIGEST = {
    "date": None,
    "news": []
}

# ------------------ UTILS ------------------

def get_published_ts(entry):
    """Robust timestamp resolver"""
    if hasattr(entry, "published_parsed"):
        return calendar.timegm(entry.published_parsed)
    if hasattr(entry, "updated_parsed"):
        return calendar.timegm(entry.updated_parsed)
    return int(time.time())


def is_recent(published_ts, source):
    max_age = SOURCE_MAX_AGE.get(source, 24 * 60 * 60)
    return (time.time() - published_ts) <= max_age


def build_summary(title: str, summary: str):
    text = (summary or "").strip()

    if len(text) < 120:
        text = f"{title}. {text}"

    text = " ".join(text.split())

    if len(text) > 350:
        text = text[:350].rsplit(" ", 1)[0] + "..."

    return text

# ------------------ FETCH NEWS ------------------

def fetch_news():
    all_news = []

    print("🔄 Starting RSS fetch")

    for source, url in RSS_FEEDS.items():
        print(f"➡️ Fetching source: {source}")

        feed = feedparser.parse(url)
        print(f"   📦 Raw entries from {source}: {len(feed.entries)}")

        limit = SOURCE_LIMITS[source]
        accepted = 0
        skipped_old = 0

        for entry in feed.entries:
            if accepted >= limit:
                break

            published_ts = get_published_ts(entry)

            if not is_recent(published_ts, source):
                skipped_old += 1
                continue

            all_news.append({
                "source": source,
                "heading": entry.title,
                "summary": build_summary(
                    entry.title,
                    getattr(entry, "summary", "")
                ),
                "link": entry.link,
                "published": time.strftime(
                    "%Y-%m-%d %H:%M",
                    time.gmtime(published_ts)
                ),
                "published_ts": published_ts
            })

            accepted += 1

        print(f"   ✅ Accepted: {accepted}, ❌ Skipped (old): {skipped_old}")

    # Sort by priority then newest
    all_news.sort(
        key=lambda x: (
            SOURCE_PRIORITY.get(x["source"], 99),
            -x["published_ts"]
        )
    )

    print(f"🧾 Total news items collected: {len(all_news)}")
    return all_news

# ------------------ BUILD DIGEST ------------------

def build_daily_digest():
    print("🛠️ Building daily digest")
    DAILY_DIGEST["news"] = fetch_news()
    DAILY_DIGEST["date"] = time.strftime("%Y-%m-%d %H:%M")
    print(f"📅 Digest updated at {DAILY_DIGEST['date']}")

# ------------------ BACKGROUND JOB ------------------

def refresh_loop():
    while True:
        try:
            print("\n📰 Refresh cycle started")
            build_daily_digest()
            print("✅ Refresh cycle completed\n")
        except Exception as e:
            print("❌ Error during refresh:", e)

        time.sleep(REFRESH_INTERVAL)

# ------------------ FASTAPI ------------------

app = FastAPI(title="Defence & World News (BBC + Hindu + Indian Defence)")

@app.on_event("startup")
def start_background_task():
    print("🚀 FastAPI started, launching background refresh thread")
    threading.Thread(
        target=refresh_loop,
        daemon=True
    ).start()

@app.get("/")
def health():
    return {
        "status": "ok",
        "sources": list(RSS_FEEDS.keys()),
        "last_updated": DAILY_DIGEST["date"]
    }

@app.get("/news")
def get_news():
    print("📤 /news endpoint called")
    return {
        "last_updated": DAILY_DIGEST["date"],
        "count": len(DAILY_DIGEST["news"]),
        "news": DAILY_DIGEST["news"]
    }
