from fastapi import APIRouter
from langsmith import traceable

from lecturette.schema import (
    LecturetteAnalysisRequest,
    LecturetteAnalysisResponse,
)
from lecturette.lecturette_chain import get_lchain

router = APIRouter(prefix="/lecturette", tags=["Lecturette"])

@traceable(
    name="Lecturette Evaluator",
    run_type="chain",
    tags=["lecturette", "fastapi", "production"],
)
@router.post("/evaluate", response_model=LecturetteAnalysisResponse)
async def evaluate_lecturette(data: LecturetteAnalysisRequest):

    lchain = get_lchain()   # ← create LLM here (runtime)

    result = await lchain.ainvoke(
        {
            "topic": data.topic,
            "userText": data.userText,
            "durationSeconds": data.durationSeconds,
        },
        config={
            "metadata": {
                "app": "ssb-lecturette",
                "endpoint": "/lecturette/evaluate",
                "duration": data.durationSeconds,
                "text_length": len(data.userText),
            }
        },
    )

    return result