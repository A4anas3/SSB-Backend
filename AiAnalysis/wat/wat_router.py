from fastapi import APIRouter
from langsmith import traceable
from typing import List

from wat.wat_schema import WatItemInput, WatAnalysisResponse
from wat.wat_graph import wat_graph

router = APIRouter(prefix="/wat", tags=["WAT"])


@traceable(
    name="WAT Evaluator",
    run_type="chain",
    tags=["fastapi", "wat", "production"],
)
@router.post("/evaluate", response_model=WatAnalysisResponse)
async def evaluate_wat(items: List[WatItemInput]):

    result = await wat_graph.ainvoke(
        {"items": items},
        config={
            "metadata": {
                "app": "ssb-wat",
                "endpoint": "/wat/evaluate",
                "total_words": len(items),
            }
        },
    )

    return result["result"]
