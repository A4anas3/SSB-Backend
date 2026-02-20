import asyncio
import json
from typing import List
from langgraph.graph import StateGraph, END

from wat.wat_schema import (
    WatItemInput,
    WatItemOutput,
    WatBatchLLMResponse
)

from wat.wat_chain import get_wat_batch_chain

chain = get_wat_batch_chain()
sem = asyncio.Semaphore(6)


async def evaluate_batch(batch: List[WatItemInput]) -> List[WatItemOutput]:
    async with sem:

        payload = [
            {
                "word": i.word,
                "sentence": i.sentence
            }
            for i in batch
        ]

        res: WatBatchLLMResponse = await chain.ainvoke({
            "items": json.dumps(payload)
        })

        outputs = []

        for item, r in zip(batch, res.items):
            outputs.append(
                WatItemOutput(
                    wordNo=item.wordNo,
                    word=item.word,
                    sentence=item.sentence,
                    grade=r.grade,
                    improvement=r.improvement
                )
            )

        return outputs


def chunk(items, size=12):
    for i in range(0, len(items), size):
        yield items[i:i + size]


async def run_parallel(items: List[WatItemInput]):

    batches = list(chunk(items, 12))
    tasks = [evaluate_batch(b) for b in batches]
    batch_results = await asyncio.gather(*tasks)

    results: List[WatItemOutput] = [
        item for batch in batch_results for item in batch
    ]

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


def build_graph():
    graph = StateGraph(dict)

    async def node(state):
        items = state["items"]
        result = await run_parallel(items)
        return {"result": result}

    graph.add_node("wat_eval", node)
    graph.set_entry_point("wat_eval")
    graph.add_edge("wat_eval", END)

    return graph.compile()


wat_graph = build_graph()
