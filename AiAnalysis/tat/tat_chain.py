import os
from langchain_openai import ChatOpenAI
from tat.tat_prompt import build_prompt_tat_batch
from tat.tat_schema import TatBatchLLMResponse

def get_tat_batch_chain():

    llm = ChatOpenAI(
        model="llama3.1-8b",
        api_key=os.getenv("CEREBRAS_TAT_KEY"),
        base_url="https://api.cerebras.ai/v1",
        temperature=0.2,
    )

    prompt = build_prompt_tat_batch()

    return prompt | llm.with_structured_output(TatBatchLLMResponse)
