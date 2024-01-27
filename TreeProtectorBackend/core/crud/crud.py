from sqlalchemy.orm import Session
from core.models import models
from core.schemas import schemas
from datetime import datetime


def create_tree_data(db: Session, tree_data: schemas.Tree):
    with db.begin():
        tree = models.Tree(
            machine_id=tree_data.machine_id,
            status=tree_data.status,
            date_created=datetime.utcnow(),
            photo_url=tree_data.photo_url,
            tree_kind=tree_data.tree_kind,
            coordinates=tree_data.coordinates
        )
        db.add(tree)
    return tree


def update_tree_data(db: Session, tree_data: schemas.Tree):
    tree_data = db.query(models.Tree).where(models.Tree.id == tree_data.request_id).first()
    tree_data.status = tree_data.status
    tree_data.coordinates = tree_data.coordinates
    tree_data.photo_url = tree_data.photo_url
    db.commit()
    db.refresh(tree_data)
    return tree_data


def get_tree_data_by_id(db: Session, tree_data_id):
    tree_data = db.query(models.Tree).where(models.Tree.id == tree_data_id).first()
    if tree_data:
        response = schemas.Tree(tree_id=tree_data_id,
                                machine_id=tree_data.machine_id,
                                status=tree_data.status,
                                date_created=tree_data.date_created,
                                photo_url=tree_data.photo_url,
                                tree_kind=tree_data.tree_kind,
                                coordinates=tree_data.coordinates)
    else:
        return None
    return response

