import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from sqlalchemy import LargeBinary
from utils.myUtils import MyUtils
from utils.dto import Dto
from common.identity import IDENTITY

from dto.t_class import t_class

from utils.app import App

from datetime import datetime
from utils.app import App


app = App.getApp()
db = App.getDB()

class t_exer(db.Model):
    __tablename__ = 't_exer'
    __table_args__ = {'extend_existing': True}

    exer_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    class_id = db.Column(db.Integer, nullable=False, default=-1)
    course_id = db.Column(db.Integer, nullable=False, default=-1)
    creator_id = db.Column(db.Integer, nullable=False)
    begin_time = db.Column(db.DateTime, nullable=True, default=datetime.utcnow)
    end_time = db.Column(db.DateTime, nullable=True)
    is_public = db.Column(db.Boolean, default=False)
    name = db.Column(db.String(255), nullable=False)
    is_multi = db.Column(db.Boolean, default=False)
    score = db.Column(db.Integer, default=0)

    def to_dict(self):
        return {
            "exer_id": self.exer_id,
            "class_id": self.class_id,
            "course_id": self.course_id,
            "creator_id": self.creator_id,
            "begin_time": self.begin_time,
            "end_time": self.end_time,
            "is_public": self.is_public,
            "name": self.name,
            "is_multi": self.is_multi,
            "score": self.score
        }
    

    # 根据课程id得到练习的列表
    def get_exerls_by_course(course_id):
        return db.session.query(t_exer).filter_by(course_id = course_id).all()
    
    # 根据练习的id得到班级的列表
    def get_classls_by_exerid(exer_id):
        classid = db.session.query(t_exer.class_id).filter_by(exer_id=exer_id).all()
        ls = []
        for i in classid:
            ls.append(db.session.query(t_class).filter_by(class_id = i).all()[0])
        return ls

