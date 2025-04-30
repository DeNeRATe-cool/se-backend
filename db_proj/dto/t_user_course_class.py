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

class t_user_course_class(db.Model):
    __tablename__ = 't_user_course_class'
    __table_args__ = {'extend_existing': True}

    user_id = db.Column(db.Integer, primary_key=True)
    course_id = db.Column(db.Integer, primary_key=True)
    class_id = db.Column(db.Integer, primary_key=True)
    identity = db.Column(db.Integer, nullable=False)

    def to_dict(self):
        return {
            "user_id": self.user_id,
            "course_id": self.course_id,
            "class_id": self.class_id,
            "identity": self.identity
        }
    
    def get_tutorls_by_course(course_id):
        tutors = db.session.query(t_user_course_class.tutor_id).filter_By(course_id = course_id,class_id = -1,identity = IDENTITY.tutor).all()
        return tutors

    def get_class_or_course(course_id=None, class_id=None):
        """
        查询 user_id=-1 的模板关系：
        - 如果给 course_id，查所有关联的 class_id
        - 如果给 class_id，查所有关联的 course_id
        
        Args:
            course_id (int, optional): 课程ID
            class_id (int, optional): 班级ID
        
        Returns:
            list: 查询到的 class_id 或 course_id 列表
        """

        if course_id is not None:
            # 给定 course_id，查 class_id 列表
            results = db.session.query(t_user_course_class.class_id).filter_by(
                user_id=-1,
                course_id=course_id
            ).all()
            return [r.class_id for r in results]

        elif class_id is not None:
            # 给定 class_id，查 course_id 列表
            results = db.session.query(t_user_course_class.course_id).filter_by(
                user_id=-1,
                class_id=class_id
            ).all()
            return [r.course_id for r in results]

        else:
            raise ValueError("必须传入 course_id 或 class_id 之一")
        
    # 根据班级得到对应的学生选课列表
    # identity = IDENTITY.student
    def get_stuid_ls_by_classid(classid:int):
        stuid_ls = db.session.query(t_user_course_class.user_id).filter_by(class_id = classid,identity=IDENTITY.student).all()
        return stuid_ls

if __name__ == '__main__':
    with app.app_context():
        ls = t_user_course_class.get_stuid_ls_by_classid(3)
        for i in ls:
            print(i[0])

