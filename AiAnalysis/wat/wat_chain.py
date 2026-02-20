import os
from langchain_openai import ChatOpenAI
from wat.wat_prompt import build_prompt_wat_batch
from wat.wat_schema import WatBatchLLMResponse

def get_wat_batch_chain():

    llm = ChatOpenAI(
        model="llama3.1-8b",
        api_key=os.getenv("CEREBRAS_WAT_KEY"),
        base_url="https://api.cerebras.ai/v1",
        temperature=0.2,
    )

    prompt = build_prompt_wat_batch()

    return prompt | llm.with_structured_output(WatBatchLLMResponse)
