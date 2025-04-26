import sys
import os

# 将项目根目录添加到 sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))

from utils.app import App
import flask_sqlalchemy

class Dto:
    def getDTOls(dto:flask_sqlalchemy.model.DefaultMeta):
        app = App.getApp()
        db = App.getDB()
        with app.app_context():
            return dto.query.all()