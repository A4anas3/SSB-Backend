import os
from langchain_openai import ChatOpenAI
from lecturette.prompt import build_prompt_lecturette
from lecturette.schema import LecturetteAnalysisResponse

def get_lchain():
    llm_lecturette = ChatOpenAI(
        model="llama3.1-8b",
        api_key=os.getenv("CEREBRAS_LECTURETTE_KEY"),
        base_url="https://api.cerebras.ai/v1",
        temperature=0.2,
    )

    structured_llm = llm_lecturette.with_structured_output(
        LecturetteAnalysisResponse,
        method="json_mode"
    )

    prompt = build_prompt_lecturette()
    return prompt | structured_llm