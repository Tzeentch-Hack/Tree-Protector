from fastapi import APIRouter, Depends, HTTPException, Request, File, UploadFile

from datetime import datetime

from core.models.database import SessionLocal
from sqlalchemy.orm import Session

from core.schemas.schemas import CreateTreeResponse, Tree
from core.models.models import Tree
from core.image_logic import image_manager

from core.crud import crud

router = APIRouter(
    prefix="/trees",
    tags=["trees"],
    responses={404: {"description": "Not found"}},
)


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@router.post("/add_new_tree")
async def add_new_tree(machine_id: str, coordinates: str, request: Request, file: UploadFile = File(...),
                       db: Session = Depends(get_db)) -> CreateTreeResponse:

    basic_url = "http://" + request.base_url.hostname + ":" + str(request.base_url.port) + "/download/"
    file_path = image_manager.save_tree_photo(file)
    photo_url = basic_url + file_path
    # TODO: Сделать определение вида дерева здесь
    tree_data = Tree(machine_id=machine_id,
                     status="new",
                     date_created=datetime.utcnow(),
                     photo_url=photo_url,
                     tree_kind=None,
                     coordinates=coordinates)

    tree_data = crud.create_tree_data(db, tree_data)
    response = CreateTreeResponse(tree_id=tree_data.id,
                                  status=tree_data.status,
                                  date_created=tree_data.date_created.strftime("%B %d, %Y"),
                                  photo_url=tree_data.photo_url,
                                  tree_kind=tree_data.tree_kind,
                                  coordinates=tree_data.coordinates)
    return response
