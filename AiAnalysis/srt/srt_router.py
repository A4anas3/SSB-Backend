from fastapi import APIRouter
from langsmith import traceable
from typing import List

from srt.srt_schema import SrtItemInput, SrtAnalysisResponse
from srt.srt_graph import srt_graph

router = APIRouter(prefix="/srt", tags=["SRT"])


@traceable(
    name="SRT Evaluator",
    run_type="chain",
    tags=["fastapi", "srt", "production"],
)
@router.post("/evaluate", response_model=SrtAnalysisResponse)
async def evaluate_srt(items: List[SrtItemInput]):

    result = await srt_graph.ainvoke(
        {"items": items},
        config={
            "metadata": {
                "app": "ssb-srt",
                "endpoint": "/srt/evaluate",
                "total_situations": len(items),
            }
        },
    )

    return result["result"]
