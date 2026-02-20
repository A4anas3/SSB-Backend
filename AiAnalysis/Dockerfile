FROM python:3.11-slim

WORKDIR /app

# Upgrade pip
RUN pip install --upgrade pip

# Install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Create a non-root user
RUN useradd -m -u 1000 user
USER user
ENV HOME=/home/user \
    PATH=/home/user/.local/bin:$PATH

# Copy application code with ownership
COPY --chown=user . .

# Expose the port (informative only)
EXPOSE 8000

# Start the application with dynamic port support (for Railway)
CMD uvicorn main:app --host 0.0.0.0 --port ${PORT:-8000}
