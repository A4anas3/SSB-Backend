from pydantic import BaseModel
from typing import List, Literal, Optional

Grade = Literal["BAD", "AVERAGE", "GOOD", "EXCELLENT"]

class TatStoryInput(BaseModel):
    image_id: int
    context: Optional[str] = ""
    story: str

class TatStoryOutput(BaseModel):
    image_id: int
    context: Optional[str]
    story: str
    grade: Grade
    improvement: str

class TatLLMResponse(BaseModel):
    grade: Grade
    improvement: str

class TatBatchLLMResponse(BaseModel):
    items: List[TatLLMResponse]
   
class TatAnalysisResponse(BaseModel):
    overall: Grade
    items: List[TatStoryOutput]
