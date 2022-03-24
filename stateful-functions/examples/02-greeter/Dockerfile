FROM python:3.9-slim-buster

ENV http_proxy http://127.0.0.1:3128
ENV https_proxy http://127.0.0.1:3128

RUN mkdir -p /app
WORKDIR /app

COPY requirements.txt /app
RUN pip install -r requirements.txt

COPY functions.py /app

EXPOSE 8000

CMD ["python3", "/app/functions.py"]