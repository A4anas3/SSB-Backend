FROM python:3.9-slim
WORKDIR /app
COPY . /app
RUN pip install fastapi uvicorn pydantic feedparser
EXPOSE 8000
CMD uvicorn main:app --host 0.0.0.0 --port ${PORT:-8000}
