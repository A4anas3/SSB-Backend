from pydantic import BaseModel, Field
from typing import Literal, List, Optional


class PPDTResponse(BaseModel):
    status: Literal["valid", "invalid"]
    final_score: float | None = Field(default=None, ge=0, le=10)
    overall_feedback: str | None = None
    improvements: str | None = None
    message: Optional[str] = None

class EvalRequest(BaseModel):
    action: str
    story: str
    image_context: str | None = ""
    age: int | None = 20
    


class StoryInput(BaseModel):
    image_id: int
    context: str
    story: str


class BatchInput(BaseModel):
    user_id: str
    stories: List[StoryInput]
