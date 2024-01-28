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


class TreeType(BaseModel):
    label: str
    score: str | float


class TreeClassifierResult(BaseModel):
    first_tree_type: TreeType
    second_tree_type: TreeType

    def to_str(self) -> str:
        return f"{self.first_tree_type.score}% {self.first_tree_type.label};" \
               f"{self.second_tree_type.score}% {self.second_tree_type.label}"


class TreeSatellitePhotoResponse(BaseModel):
    colored_photo_url: str
    binary_photo_url: str

