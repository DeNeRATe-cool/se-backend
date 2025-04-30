import sys
import os

# 将项目根目录添加到 sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))

import random
from utils.app import App


app = App.getApp()
db = App.getDB()

class t_class(db.Model):
    __table_args__ = {'extend_existing':True}
    class_id = db.Column(db.Integer,primary_key = True)
    name = db.Column(db.String(255))
    course_id = db.Column(db.Integer)
    class_code = db.Column(db.String(255))

    def to_dict(self):
        return {
            "class_id":self.class_id,
            "name":self.name,
            "course_id":self.course_id,
            "class_code":self.class_code
        }
    
    def generateRandomClassName():
        class_names = [
            # === 名校/精英类 ===
            "清华班", "北大班", "哈佛班", "牛津班", "剑桥班",
            "常春藤班", "985班", "211班", "C9班", "双一流班",
            
            # === 速度/力量类 ===
            "闪电班", "雷霆班", "疾风班", "烈火班", "战狼班",
            "猛虎班", "雄狮班", "猎豹班", "天马班", "飞龙班",
            
            # === 神兽/神话类 ===
            "麒麟班", "凤凰班", "鲲鹏班", "青龙班", "白虎班",
            "朱雀班", "玄武班", "貔貅班", "饕餮班", "白泽班",
            
            # === 志向高远类 ===
            "凌云班", "翱翔班", "鸿鹄班", "星海班", "银河班",
            "苍穹班", "北斗班", "天穹班", "银河班", "寰宇班",
            
            # === 学术/文化类 ===
            "博雅班", "明德班", "格物班", "知行班", "求是班",
            "致远班", "弘毅班", "慎思班", "睿智班", "翰林班",
            
            # === 自然元素类 ===
            "旭日班", "皓月班", "星辰班", "沧海班", "青山班",
            "松柏班", "翠竹班", "寒梅班", "云霞班", "晨露班",
            
            # === 科技/未来类 ===
            "量子班", "AI班", "星际班", "未来班", "探索班",
            "创新班", "智远班", "极客班", "算法班", "元宇宙班",
            
            # === 军事/纪律类 ===
            "铁血班", "钢刃班", "尖刀班", "先锋班", "突击班",
            "战鹰班", "雷霆班", "铁骑班", "神盾班", "利剑班",
            
            # === 励志/正能量类 ===
            "追梦班", "启航班", "超越班", "巅峰班", "荣耀班",
            "奇迹班", "奋进班", "拼搏班", "凯旋班", "王者班",
            
            # === 中国风/古韵类 ===
            "翰林班", "状元班", "探花班", "榜眼班", "尚书班",
            "太学班", "国子监班", "稷下班", "兰亭班", "墨韵班"
        ]
        return random.choice(class_names)