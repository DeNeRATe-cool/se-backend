import sys
import os
from sqlalchemy import LargeBinary
from utils.myUtils import MyUtils
from utils.dto import Dto
from common.probType import ProbType

# 将项目根目录添加到 sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))

import random
from utils.app import App
from common.identity import IDENTITY


app = App.getApp()
db = App.getDB()

answer_ls = [
    "计算机网络是由多台计算机通过通信线路连接起来，实现数据交换和资源共享的系统。",
    "网络就是很多电脑连接在一起，可以互相传文件、聊天什么的。",
    "计算机网络是信息交换的一种方式，比如互联网就是一个很大的计算机网络。",
    "它可以让不同地区的人互相发送邮件、浏览网页。",
    "就是把电脑通过网线、无线的方式连起来，可以分享信息和资源。"
]

code_answer_ls = [
    "def is_palindrome(s):\n    return s == s[::-1]",
    "def is_palindrome(s):\n    if s == s[::-1]:\n        return True\n    else:\n        return False",
    "def is_palindrome(s):\n    return s == ''.join(reversed(s))",
    "def is_palindrome(s):\n    for i in range(len(s) // 2):\n        if s[i] != s[-(i+1)]:\n            return False\n    return True",
    "def is_palindrome(string):\n    return string == string[::-1]"
]


class t_prob(db.Model):
    __table_attribute__ = {'extend_existing':True}
    prob_id = db.Column(db.Integer,primary_key=True,autoincrement=True)
    is_public = db.Column(db.Boolean)
    type = db.Column(db.Integer)
    creator_id = db.Column(db.Integer)
    create_time = db.Column(db.DateTime)
    description = db.Column(db.String(255))
    content = db.Column(db.String(255))
    answer = db.Column(db.String(255))
    analysis = db.Column(db.String(255))
    tag_ls = db.Column(db.String(255))

    def to_dict(self):
        return {
            "prob_id":self.prob_id,
            "is_public":self.is_public,
            "type":self.type,
            "creator_id":self.creator_id,
            "create_time":self.create_time,
            "description":self.description,
            "content":self.content,
            "answer":self.answer,
            "analysis":self.analysis,
            "tag_ls":self.tag_ls
        }
    
    def get_prob_idls():
        ls = Dto.getDTOls(t_prob)
        return [i.prob_id for i in ls]
    
    """
        生成随机的回答
        结合题目的种类

        Parameters:
        type:题目种类

        Returns
        submit:str 用户提交
    """
    def gen_answer_by_type(type:int):
        CHOICE = ['A','B','C','D']
        JUDGE = ['true','false']
        if type == ProbType.choice:
            return random.choice(CHOICE)
        elif type == ProbType.judge:
            return random.choice(JUDGE)
        elif type == ProbType.subject:
            return random.choice(answer_ls)
        elif type == ProbType.code:
            return random.choice(code_answer_ls)
        
    def get_prob_by_id(prob_id):
        return db.session.query(t_prob).filter_by(prob_id=prob_id).all()[0]
    
