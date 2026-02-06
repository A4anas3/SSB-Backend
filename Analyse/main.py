from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class PPDTRequest(BaseModel):
    image_context: str
    story_text: str


@app.post("/analyze/ppdt")
def analyze_ppdt(req: PPDTRequest):

    return {
        # CORE PERCEPTION
        "number_of_characters": 2,
        "basic_details_identified": True,
        "perception_score": 7.5,

        # STORY QUALITY
        "logical_and_complete": True,
        "positive_and_realistic": True,
        "story_score": 8.0,

        # EXPRESSION
        "word_count": 120,
        "clarity_score": 7.8,

        # OLQ SIGNAL
        "officer_like_thinking_score": 7.6,

        # FINAL RESULT
        "overall_ppdt_score": 7.7,
        "feedback": "Good perception and positive flow. Improve clarity and a more decisive conclusion."
    }
