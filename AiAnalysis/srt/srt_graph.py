import asyncio
import json
from typing import List

from langgraph.graph import StateGraph, END

from srt.srt_schema import (
    SrtItemInput,
    SrtItemOutput,
    SrtLLMResponse,
    SrtBatchLLMResponse
)

from srt.srt_chain import get_srt_batch_chain


# LLM chain (batch version)
chain = get_srt_batch_chain()

# parallel batch limiter
sem = asyncio.Semaphore(6)  


# ---------- batch evaluator ----------
async def evaluate_batch(batch: List[SrtItemInput]) -> List[SrtItemOutput]:
    async with sem:

        payload = [
            {
                "situation": i.situation,
                "reaction": i.reaction
            }
            for i in batch
        ]

        res: SrtBatchLLMResponse = await chain.ainvoke({
            "items": json.dumps(payload)
        })

        outputs = []

        for item, r in zip(batch, res.items):
            outputs.append(
                SrtItemOutput(
                    situationNo=item.situationNo,
                    situation=item.situation,
                    reaction=item.reaction,
                    grade=r.grade,
                    improvement=r.improvement
                )
            )

        return outputs


# ---------- split into batches ----------
def chunk(items, size=10):
    for i in range(0, len(items), size):
        yield items[i:i + size]


# ---------- main parallel runner ----------
async def run_parallel(items: List[SrtItemInput]):

    batches = list(chunk(items, 12))

    tasks = [evaluate_batch(b) for b in batches]

    batch_results = await asyncio.gather(*tasks)

    # flatten
    results: List[SrtItemOutput] = [
        item for batch in batch_results for item in batch
    ]

    # scoring
    score_map = {
        "BAD": 1,
        "AVERAGE": 2,
        "GOOD": 3,
        "EXCELLENT": 4
    }

    avg = sum(score_map[r.grade] for r in results) / len(results)

    if avg < 1.8:
        overall = "BAD"
    elif avg < 2.5:
        overall = "AVERAGE"
    elif avg < 3.3:
        overall = "GOOD"
    else:
        overall = "EXCELLENT"

    return {
        "overall": overall,
        "items": results
    }


# ---------- langgraph ----------
def build_graph():
    graph = StateGraph(dict)

    async def node(state):
        items = state["items"]
        result = await run_parallel(items)
        return {"result": result}

    graph.add_node("srt_eval", node)
    graph.set_entry_point("srt_eval")
    graph.add_edge("srt_eval", END)

    return graph.compile()


srt_graph = build_graph()
