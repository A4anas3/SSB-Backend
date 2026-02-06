FROM python:3.9-slim
WORKDIR /app
COPY . /app
RUN pip install fastapi uvicorn feedparser
EXPOSE 10000
CMD ["uvicorn","main:app","--host","0.0.0.0","--port","10000"]
