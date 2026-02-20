from dotenv import load_dotenv
load_dotenv(".env")  # load once, before any imports that use os.getenv

import os
from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException, Depends, Security
from fastapi.security import APIKeyHeader
from fastapi.middleware.cors import CORSMiddleware



from ppdt.ppdt_router import router as ppdt_router
from lecturette.lecturette_router import router as lecturette_router
from srt.srt_router import router as srt_router
from wat.wat_router import router as wat_router
from tat.tat_router import router as tat_router
from news.news_router import router as news_router
from news.news_service import start_news_refresh


# ========================
# API KEY AUTHENTICATION
# ========================

API_KEY = os.getenv("SSB_API_KEY", "")
API_KEY_HEADER = APIKeyHeader(name="X-API-Key", auto_error=False)


async def verify_api_key(api_key: str = Security(API_KEY_HEADER)):
    """Verify the API key from the X-API-Key header."""
    if not API_KEY:
        # If no API key is configured, allow all requests (dev mode)
        return None
    if api_key != API_KEY:
        raise HTTPException(status_code=403, detail="Invalid or missing API key")
    return api_key





# ========================
# LIFESPAN (replaces deprecated on_event)
# ========================

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup
    start_news_refresh()
    yield
    # Shutdown (nothing to clean up)


# ========================
# APP SETUP
# ========================

app = FastAPI(title="SSB AI Backend", version="1.0", lifespan=lifespan)

# CORS — restrict to your Spring Boot backend and frontend
# CORS — restrict to your Spring Boot backend and frontend
ALLOWED_ORIGINS = [
    origin.strip()
    for origin in os.getenv("ALLOWED_ORIGINS", "http://localhost:9000,http://localhost:5173").split(",")
    if origin.strip()
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["GET", "POST"],
    allow_headers=["Authorization", "Content-Type", "X-API-Key"],
)



# ========================
# ROUTERS (all require API key)
# ========================

app.include_router(ppdt_router, dependencies=[Depends(verify_api_key)])
app.include_router(lecturette_router, dependencies=[Depends(verify_api_key)])
app.include_router(wat_router, dependencies=[Depends(verify_api_key)])
app.include_router(srt_router, dependencies=[Depends(verify_api_key)])
app.include_router(tat_router, dependencies=[Depends(verify_api_key)])
app.include_router(news_router, dependencies=[Depends(verify_api_key)])