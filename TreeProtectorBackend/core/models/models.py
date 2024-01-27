from core.models.database import Base

from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, DateTime
from datetime import datetime


class Request(Base):
    __tablename__ = "trees"

    id = Column(Integer, primary_key=True)
    machine_id = Column(String)
    status = Column(String, default=None)
    date_created = Column(DateTime, default=datetime.utcnow)
    photo_url = Column(String, default=None)
    tree_kind = Column(String, default=None)