package com.se.service;

import com.github.pagehelper.PageInfo;
import com.se.dto.AddAdminInCourseDTO;
import com.se.entity.Class;
import com.se.entity.Course;
import com.se.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseService {

    Course add(Course course);

    List<User> addAdmin(AddAdminInCourseDTO addAdminDTO);

    List<User> listTeacherAndTutor(Integer courseId);

    Course courseInfo(Integer courseId);

    List<Class> listByCourseId(Integer courseId, Integer userId);

    List<List<Course>> listSelf(Integer userId);

    List<Class> getUserClass(Integer courseId, Integer userId);

    List<Course> list();

    PageInfo<Course> listPage(Integer page, Integer size);
}
