from fastapi import APIRouter, Depends, HTTPException, Request

router = APIRouter(
    prefix="/trees/TTS",
    tags=["TTS"],
    responses={404: {"description": "Not found"}},
)