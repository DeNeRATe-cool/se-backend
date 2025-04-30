import sys
import os

# 将项目根目录添加到 sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))

import random
from utils.app import App
from common.identity import IDENTITY


app = App.getApp()
db = App.getDB()

class t_user(db.Model):
    __table_args__ = {'extend_existing':True}
    user_id = db.Column(db.Integer, primary_key=True, autoincrement= True)
    username = db.Column(db.String(255))
    password = db.Column(db.String(255))
    name = db.Column(db.String(255))
    mail = db.Column(db.String(255))
    birthday = db.Column(db.Date)
    identity = db.Column(db.Integer)

    def to_dic(self):
        return{
            'user_id':self.user_id,
            'username':self.username,
            'password':self.password,
            'name':self.name,
            'mail':self.mail,
            'birthday':self.birthday,
            'identity':self.identity
        }
    
    def get_random_teacher_id():
        with app.app_context():
            ls = t_user.query.filter_by(identity=IDENTITY.teacher).all()
        return random.choice(ls).user_id
    
    def get_teacher_ls():
        with app.app_context():
            ls = t_user.query.filter_by(identity=IDENTITY.teacher).all()
            return ls

    def get_student_ls():
        with app.app_context():
            ls = t_user.query.filter_by(identity=IDENTITY.student).all()
            return ls
        