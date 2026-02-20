
import os

from langchain_openai import ChatOpenAI
from ppdt.prompt_builder import build_prompt
from ppdt.schemas import PPDTResponse



# 🟢 Use smaller model for free tier
llm = ChatOpenAI(
    model="llama3.1-8b",   # cheapest
    api_key=os.getenv("CEREBRAS_PPDT_KEY"),
    base_url="https://api.cerebras.ai/v1",
    temperature=0.2,
)

structured_llm = llm.with_structured_output(PPDTResponse)

prompt = build_prompt()

chain = prompt | structured_llm