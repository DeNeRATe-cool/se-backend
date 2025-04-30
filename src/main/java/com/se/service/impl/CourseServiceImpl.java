package com.se.service.impl;

import com.se.dao.CourseDao;
import com.se.entity.Course;
import com.se.exception.classException.DuplicateCourseException;
import com.se.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;

    public void add(Course course)
    {
        List<Course> res = courseDao.getCourseByName(course.getName());
        if(res.size()!=0)throw new DuplicateCourseException("课程名已经存在");
        courseDao.add(course);
    }

}
