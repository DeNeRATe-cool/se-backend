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

class t_tag(db.Model):
    __tablename__ = 't_tag'
    __table_args__ = {'extend_existing': True}

    tag_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(255), nullable=False)

    def to_dict(self):
        return {
            "tag_id": self.tag_id,
            "name": self.name
        }

    def get_common_tag_ls():
        tags = [
            # 计算机学科知识点
            "数据结构", "算法", "操作系统", "计算机网络", "数据库",
            "编程范式", "软件工程", "计算机组成原理", "编译原理", 
            "人工智能", "机器学习", "深度学习", "计算机视觉", 
            "自然语言处理", "网络安全", "密码学", "分布式系统",
            "云计算", "大数据", "物联网", "区块链", "前端开发",
            "后端开发", "移动开发", "测试开发", "嵌入式系统",
            "图形学", "人机交互", "Python", "Java", "C++",
            "JavaScript", "SQL", "Linux系统", "Git版本控制",
            
            # 考试与应用场景
            "常考算法", "面试高频", "笔试真题", "易错题", 
            "大厂题库", "LeetCode风格", "ACM竞赛", "代码优化",
            "边界条件", "时间复杂度", "空间复杂度", "代码陷阱",
            
            # 难度与题型
            "基础语法", "手写代码", "系统设计", "逻辑思维", 
            "调试技巧", "多线程", "内存管理", "递归问题",
            "动态规划", "贪心算法", "二叉树遍历", "链表操作",
            "字符串处理", "数组技巧", "哈希表应用", "正则表达式"
        ]
        return tags

    def get_tag_random_ls():
        return random.sample(t_tag.get_common_tag_ls(),random.randint(1,5))
    

if __name__ == '__main__':
    print(type(t_tag.get_tag_random_ls()))