from pydantic import BaseModel
from typing import List, Literal

Grade = Literal["BAD", "AVERAGE", "GOOD", "EXCELLENT"]

class WatItemInput(BaseModel):
    wordNo: int
    word: str
    sentence: str


class WatItemOutput(BaseModel):
    wordNo: int
    word: str
    sentence: str
    grade: Grade
    improvement: str


class WatAnalysisResponse(BaseModel):
    overall: Grade
    items: List[WatItemOutput]


class WatLLMResponse(BaseModel):
    grade: Grade
    improvement: str


class WatBatchLLMResponse(BaseModel):
    items: List[WatLLMResponse]
