import asyncio
import json
from typing import List

from tat.tat_schema import TatStoryInput, TatStoryOutput
from tat.tat_chain import get_tat_batch_chain

chain = get_tat_batch_chain()
sem = asyncio.Semaphore(5)


async def evaluate_batch(batch: List[TatStoryInput]):
    async with sem:

        payload = [
            {
                "context": i.context or "",
                "story": i.story
            }
            for i in batch
        ]

        res = await chain.ainvoke({
            "items": json.dumps(payload)
        })

        outputs = []

        for item, r in zip(batch, res.items):
            outputs.append(
                TatStoryOutput(
                    image_id=item.image_id,
                    context=item.context,
                    story=item.story,
                    grade=r.grade,
                    improvement=r.improvement
                )
            )

        return outputs


def chunk(items, size=4):
    for i in range(0, len(items), size):
        yield items[i:i+size]


async def run_parallel(items: List[TatStoryInput]):

    batches = list(chunk(items, 4))
    tasks = [evaluate_batch(b) for b in batches]

    batch_results = await asyncio.gather(*tasks)

    results = [i for b in batch_results for i in b]

    score_map = {
        "BAD": 1,
        "AVERAGE": 2,
        "GOOD": 3,
        "EXCELLENT": 4
    }

    avg = sum(score_map[r.grade] for r in results)/len(results)

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

from langgraph.graph import StateGraph, END
from tat.tat_graph import run_parallel

def build_graph():
    graph = StateGraph(dict)

    async def node(state):
        items = state["items"]
        result = await run_parallel(items)
        return {"result": result}

    graph.add_node("tat_eval", node)
    graph.set_entry_point("tat_eval")
    graph.add_edge("tat_eval", END)

    return graph.compile()

tat_graph = build_graph()

