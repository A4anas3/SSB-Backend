from fastapi import APIRouter

from news.news_service import get_daily_digest, RSS_FEEDS

router = APIRouter(prefix="/news", tags=["News"])


@router.get("")
def get_news():
    """Return the latest cached news digest."""
    print("📤 /news endpoint called")
    digest = get_daily_digest()
    return {
        "last_updated": digest["date"],
        "count": len(digest["news"]),
        "news": digest["news"]
    }


@router.get("/health")
def news_health():
    """Health check for the news module."""
    digest = get_daily_digest()
    return {
        "status": "ok",
        "sources": list(RSS_FEEDS.keys()),
        "last_updated": digest["date"]
    }
