import sys
import os
from sqlalchemy import LargeBinary

# 将项目根目录添加到 sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))

import random
from utils.app import App
from common.identity import IDENTITY


app = App.getApp()
db = App.getDB()

class t_resource(db.Model):
    __table_attribute__ = {'extend_existing':True}
    res_id = db.Column(db.Integer,primary_key=True,autoincrement=True)
    res_code = db.Column(db.String(255))
    name = db.Column(db.String(255))
    process_id = db.Column(db.Integer)
    course_id = db.Column(db.Integer)
    type = db.Column(db.String(255))
    data = db.Column(LargeBinary)
    date = db.Column(db.DateTime)
    tag = db.Column(db.String(255))

    def to_dict(self):
        return {
            "res_id":self.res_id,
            "res_code":self.res_code,
            "name":self.name,
            "process_id":self.process_id,
            "course_id":self.course_id,
            "type":self.type,
            "data":self.data,
            "date":self.date,
            "tag":self.tag
        }
    
    def file2LargeBinary(filePath):
        with open(filePath,'rb') as f:
            return f.read()
        
    def largeBinaryWriteInFile(bin,filePath):
        with open(filePath,'wb') as f:
            f.write(bin)

if __name__ == '__main__':
    f = t_resource.file2LargeBinary('./resources/a.pdf')
    t_resource.largeBinaryWriteInFile(f,'./test/b.pdf')
