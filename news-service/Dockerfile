FROM python:3.9-slim

WORKDIR /app
COPY . /app

RUN pip install fastapi uvicorn pydantic feedparser

EXPOSE 8080

CMD ["sh", "-c", "uvicorn main:app --host 0.0.0.0 --port $PORT"]
