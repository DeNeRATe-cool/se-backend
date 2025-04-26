import sys
import os

# 将项目根目录添加到 sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))

import random
from utils.app import App


app = App.getApp()
db = App.getDB()

class t_course(db.Model):
    __table_args__ = {'extend_existing':True}
    course_id = db.Column(db.Integer, primary_key=True, autoincrement= True)
    name = db.Column(db.String(255))
    creator_id = db.Column(db.Integer)
    syllabus = db.Column(db.Text)
    assMethod = db.Column(db.String(255))
    score = db.Column(db.Double)
    time = db.Column(db.Integer)

    def to_dict(self):
        return {
            "course_id":self.course_id,
            "name" :self.name,
            "creator_id":self.creator_id,
            "syllabus":self.syllabus,
            "assMethod" :self.assMethod,
            "score" : self.score,
            "time" : self.time
        }
    
    def getRandomCourseID():
        with app.app_context():
            ls = t_course.query.all()
            return random.choice(ls).course_id


if __name__ == '__main__':
    print(t_course.getRandomCourseID())