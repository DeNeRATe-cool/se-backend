from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy

class App:
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://SE_teamsix:ilovese666@47.94.184.148:3307/course_management?charset=utf8mb4'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

    db = SQLAlchemy(app)
    def getApp():
        return App.app
    
    def getDB():
        return App.db