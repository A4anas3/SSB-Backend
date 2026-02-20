from langchain_core.prompts import ChatPromptTemplate

def build_prompt_tat_batch():
    return ChatPromptTemplate.from_template("""
You are an SSB psychologist evaluating TAT stories.

For EACH story:
- realistic
- clear hero
- problem → action → outcome
- OLQ reflection
- age appropriate
one picture does not need image context, but if it has, story should be connected.

GRADES:
EXCELLENT
GOOD
AVERAGE
BAD

Return JSON:
{{
 "items":[
   {{
    "grade":"GOOD",
    "improvement":"short tip"
   }}
 ]
}}

Stories:
{items}
""")
