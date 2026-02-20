from fastapi import APIRouter
from langsmith import traceable
from typing import List

from tat.tat_schema import TatStoryInput, TatAnalysisResponse
from tat.tat_graph import tat_graph

router = APIRouter(prefix="/tat", tags=["TAT"])


@traceable(
    name="TAT Evaluator",
    run_type="chain",
    tags=["fastapi", "tat", "production"],
)
@router.post("/evaluate", response_model=TatAnalysisResponse)
async def evaluate_tat(items: List[TatStoryInput]):

    result = await tat_graph.ainvoke(
        {"items": items},
        config={
            "metadata": {
                "app": "ssb-tat",
                "endpoint": "/tat/evaluate",
                "total_images": len(items),
            }
        },
    )

    return result["result"]
