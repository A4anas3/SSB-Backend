from pydantic import BaseModel
from typing import List, Literal

Grade = Literal["BAD", "AVERAGE", "GOOD", "EXCELLENT"]

class SrtItemInput(BaseModel):
    situationNo: int
    situation: str
    reaction: str


class SrtItemOutput(BaseModel):
    situationNo: int
    situation: str
    reaction: str
    grade: Grade
    improvement: str
  


class SrtAnalysisResponse(BaseModel):
    overall: Grade
    items: List[SrtItemOutput]

class SrtLLMResponse(BaseModel):
    grade: Grade
    improvement: str

class SrtBatchLLMResponse(BaseModel):
    items: List[SrtLLMResponse]

    
