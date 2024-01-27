import os

from fastapi import FastAPI
from fastapi.responses import FileResponse
import uvicorn

from core.models.database import engine
from core.models import models
import APIs.routes

app = FastAPI()

app.include_router(APIs.routes.router)

models.Base.metadata.create_all(bind=engine)


@app.get("/download/{file_name:path}", tags=['main'])
def download_file(file_name):
    return FileResponse(path=file_name, filename=file_name.split('.')[-2],
                        media_type='audio')


@app.get("/", tags=['main'])
async def root():
    return {"message": "Tree protector Hackaton project"}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)