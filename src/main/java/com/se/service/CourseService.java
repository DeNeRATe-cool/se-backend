package com.se.service;

import com.se.dto.AddAdminInCourseDTO;
import com.se.entity.Course;
import com.se.entity.User;

import java.util.List;

public interface CourseService {


    void add(Course course);

    List<User> addAdmin(AddAdminInCourseDTO addAdminDTO);

    List<User> listTeacherAndTutor(Integer courseId);

    Course courseInfo(Integer courseId);
}
