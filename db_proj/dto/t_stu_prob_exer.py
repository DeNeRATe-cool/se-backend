import sys
import os
import random

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from sqlalchemy import LargeBinary
from utils.myUtils import MyUtils
from utils.dto import Dto
from common.identity import IDENTITY

from utils.app import App

app = App.getApp()
db = App.getDB()

class t_stu_prob_exer(db.Model):
    __tablename__ = 't_stu_prob_exer'
    __table_args__ = {'extend_existing': True}

    stu_id = db.Column(db.Integer, primary_key=True)
    prob_id = db.Column(db.Integer, primary_key=True)
    exer_id = db.Column(db.Integer, primary_key=True)

    score = db.Column(db.Integer)
    comment = db.Column(db.String(255))
    submit = db.Column(db.String(255))
    is_finish = db.Column(db.Boolean, default=False)
    is_check = db.Column(db.Boolean, default=False)
    idx = db.Column(db.Integer)

    def to_dict(self):
        return {
            "stu_id": self.stu_id,
            "prob_id": self.prob_id,
            "exer_id": self.exer_id,
            "score": self.score,
            "comment": self.comment,
            "submit": self.submit,
            "is_finish": self.is_finish,
            "is_check": self.is_check,
            "idx": self.idx
        }
    
    # 得到任务 - 学生的pair组

    def stu_exer_pair_ls():
        """
        得到列表 列表的元素为 (exer_id,stu_id)

        Args:
            NULL

        Returns:
            list<tuple>
        """
        with app.app_context():
            ls = db.session.query(t_stu_prob_exer).filter(t_stu_prob_exer.stu_id != -1,t_stu_prob_exer.prob_id == -1).all()
            res = []
            for i in ls:
                res.append((i.exer_id,i.stu_id))
            return res


    """
        根据任务 id 得到对应的题目id列表

        Args:
            exer_id:int

        Returns:
            list<prob_id:int>
    """        
    def get_prob_id_list_by_exer_id(exer_id:int):
        prob_exer_ls = db.session.query(t_stu_prob_exer).filter(t_stu_prob_exer.stu_id==-1,t_stu_prob_exer.exer_id==exer_id).all()
        res = []
        for e in prob_exer_ls:
            res.append(e.prob_id)
        return res
    
    def get_random_comment():
        comment_ls = [
            "再想想呢",
            "很接近了",
            "加油，再仔细一点！",
            "思路是对的，继续努力！",
            "不错哦，注意细节",
            "基础掌握得不错",
            "还差一点点，加油！",
            "答案有点偏了，重新审题试试",
            "继续加油！再多想一想",
            "快要答对了，再检查下过程",
            "不错，注意表达更准确",
            "思路正确，细节要补充",
            "看得出你很努力！",
            "多注意审题要求",
            "逻辑上还有一点小问题",
            "思考方向对了！",
            "可以再完善一下答案",
            "整体不错，再优化一下",
            "理解基本正确",
            "答题时要更细心一点"
        ]
        return random.choice(comment_ls)




if __name__ == '__main__':
    ls = t_stu_prob_exer.stu_exer_pair_ls()
    print(ls)