import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__),'../')))

from utils.app import App

app = App.getApp()
db = App.getDB()

class t_process(db.Model):
    __table_args__ = {'extend_existing':True}
    process_id = db.Column(db.Integer,primary_key=True,autoincrement=True)
    name = db.Column(db.String(255))
    course_id = db.Column(db.Integer)
    class_id = db.Column(db.Integer)
    time = db.Column(db.Date)

    def to_dict(self):
        return {
            "process_id":self.process_id,
            "name":self.name,
            "course_id":self.course_id,
            "class_id":self.class_id,
            "time":self.time
        }