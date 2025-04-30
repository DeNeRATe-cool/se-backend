import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from sqlalchemy import LargeBinary
from utils.myUtils import MyUtils
from utils.dto import Dto
from common.identity import IDENTITY

from utils.app import App

app = App.getApp()
db = App.getDB()

class t_collect(db.Model):
    __tablename__ = 't_collect'
    __table_args__ = {'extend_existing': True}

    stu_id = db.Column(db.Integer, primary_key=True)
    prob_id = db.Column(db.Integer, primary_key=True)

    def to_dict(self):
        return {
            "stu_id": self.stu_id,
            "prob_id": self.prob_id
        }
