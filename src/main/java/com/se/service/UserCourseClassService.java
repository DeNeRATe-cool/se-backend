package com.se.service;

import com.se.entity.User;

import java.util.List;

public interface UserCourseClassService {
    public Boolean teacherInCourse(User user, Integer course_id);
    public List<User> getUserListByCourseAndIdentity(Integer course_id, Integer identity);
    public List<User> getTutorListByCourse(Integer course_id);
    public List<User> getTeacherListByCourse(Integer course_id);
    public Boolean tutorInCourse(User user, Integer course_id);
    public List<User> getAdminListByClass(Integer class_id);
    public Boolean isCourseAndClassMatch(Integer course_id, Integer class_id);
}
