package com.se.controller;

import com.se.dto.AddAdminInCourseDTO;
import com.se.dto.Result;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public Result add(@RequestBody @Validated Course course)
    {
        courseService.add(course);
        return Result.ok();
    }

    @PostMapping("/addAdmin")
    public Result addAdmin(@RequestBody @Validated AddAdminInCourseDTO addAdminDTO)
    {
        List<User>res = courseService.addAdmin(addAdminDTO);
        return Result.ok(res,res.size());
    }

    @GetMapping("/admins")
    public Result listTeacherAndTutor(Integer course_id)
    {
        List<User> res = courseService.listTeacherAndTutor(course_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/info")
    public Result courseInfo(Integer course_id)
    {
        Course course = courseService.courseInfo(course_id);
        return Result.ok(course);
    }

}
