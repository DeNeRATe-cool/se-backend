package com.se.service;

import com.se.dto.AddAdminInCourseDTO;
import com.se.entity.Class;
import com.se.entity.Course;
import com.se.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseService {

    void add(Course course);

    List<User> addAdmin(AddAdminInCourseDTO addAdminDTO);

    List<User> listTeacherAndTutor(Integer courseId);

    Course courseInfo(Integer courseId);

    List<Class> listByCourseId(Integer courseId, Integer userId);
}
