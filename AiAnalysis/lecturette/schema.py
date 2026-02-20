from pydantic import BaseModel 
class LecturetteAnalysisRequest(BaseModel):
    topic: str
    userText: str
    durationSeconds: int 


class LecturetteAnalysisResponse(BaseModel):
    confidence: str
    clarity: str
    structure: str
    suggestions: str
    score: float
    overall: str
   