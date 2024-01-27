from pydantic import BaseModel


class Tree(BaseModel):
    tree_id: int | None
    machine_id: str | None
    status: str | None
    date_created: str | None
    photo_url: str | None
    tree_kind: str | None
    coordinates: str | None


class CreateTreeResponse(BaseModel):
    tree_id: int | str | None
    status: str | None
    date_created: str | None
    photo_url: str | None
    tree_kind: str | None
    coordinates: str | None


class TreeDataList(BaseModel):
    data: list[Tree]
