import ws
import rest

import uvicorn

from dotenv import load_dotenv
from fastapi import FastAPI

load_dotenv(".env")

app = FastAPI(title="Application")

app.include_router(ws.router)
app.include_router(rest.router)

# app.docs_url  = None
# app.redoc_url = None

@app.get("/")
async def index():
    return {"message": "Hello, FastAPI!"}

@app.get("/index", name = "index", summary = "首页")
async def index(id: int, value: str = None):
    return {"id": id, "value": value}

"""
uvicorn main:app --reload
http://127.0.0.1:8000/docs
http://127.0.0.1:8000/redoc
"""

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host = "0.0.0.0",
        port = 8000,
#       workers    = multiprocessing.cpu_count(),
        workers    = 1,
        reload     = True,
        log_level  = "debug",
        log_config = "logger.yml",
        access_log = True,
    )
