import os
from langchain_openai import ChatOpenAI
from srt.srt_prompt import build_prompt_srt_batch
from srt.srt_schema import  SrtBatchLLMResponse

def get_srt_batch_chain():

    llm = ChatOpenAI(
        model="llama3.1-8b",
        api_key=os.getenv("CEREBRAS_SRT_KEY"),
        base_url="https://api.cerebras.ai/v1",
        temperature=0.2,
    )

    prompt = build_prompt_srt_batch()

    return prompt | llm.with_structured_output(SrtBatchLLMResponse)