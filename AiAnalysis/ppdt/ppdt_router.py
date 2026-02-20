from fastapi import APIRouter
from langsmith import traceable

from ppdt.chain import chain
from ppdt.schemas import EvalRequest, PPDTResponse   # if you have response schema


router = APIRouter(prefix="", tags=["PPDT"])


@traceable(
    name="PPDT Evaluator",
    run_type="chain",
    tags=["fastapi", "ppdt", "production"],
)
@router.post("/evaluate", response_model=PPDTResponse)  # remove response_model if not using
async def evaluate(data: EvalRequest):

    result = await chain.ainvoke(
        data.model_dump(),
        config={
            "metadata": {
                "app": "ssb-ppdt",
                "endpoint": "/evaluate",
                "age": data.age,
                "story_length": len(data.story),
                "image_context": data.image_context,
            }
        },
    )
  

    return result