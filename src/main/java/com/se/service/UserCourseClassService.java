package com.se.service;

import com.se.entity.Course;
import com.se.entity.User;

import java.util.List;

public interface UserCourseClassService {
    // 老师是否在课程中
    public Boolean teacherInCourse(User user, Integer course_id);
    // 课程id + 身份 获得用户名单
    public List<User> getUserListByCourseAndIdentity(Integer course_id, Integer identity);
    // 课程助教名单
    public List<User> getTutorListByCourse(Integer course_id);
    // 课程老师名单
    public List<User> getTeacherListByCourse(Integer course_id);
    // 是否为目标课程助教
    public Boolean tutorInCourse(User user, Integer course_id);
    // 班级老师助教名单
    public List<User> getAdminListByClass(Integer class_id);
    // 课程和班级是否匹配
    public Boolean isCourseAndClassMatch(Integer course_id, Integer class_id);
    // 课程老师助教名单
    public List<User> getAdminListByCourse(Integer courseId);
    // 检查用户是否存在
    public Boolean userExist(Integer user_id);
    // 用户是学生
    public Boolean userIsStudent(Integer user_id);
    // 检查班级是否存在
    public Boolean classExist(Integer class_id);
    public Boolean classExist(String class_code);
    // 学生是否在班级中
    public Boolean studentInCourse(Integer user_id, Integer course_id);

}
