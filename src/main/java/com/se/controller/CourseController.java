package com.se.controller;

import com.github.pagehelper.PageInfo;
import com.se.dto.AddAdminInCourseDTO;
import com.se.dto.Result;
import com.se.entity.Class;
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

    @GetMapping("/all")
    public Result list()
    {
        List<Course> res = courseService.list();
        return Result.ok(res,res.size());
    }

    @GetMapping("/all/page")
    public Result listPage(Integer page, Integer size)
    {
        PageInfo<Course> res = courseService.listPage(page,size);
        return Result.ok(res);
    }

    @PostMapping("/create")
    public Result add(@RequestBody @Validated Course course)
    {
        Course res = courseService.add(course);
        return Result.ok(res);
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

    @GetMapping("/classes")
    public Result listByCourseId(Integer course_id, Integer user_id)
    {
        List<Class> res = courseService.listByCourseId(course_id,user_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/self")
    public Result listSelf(Integer user_id)
    {
        return Result.ok(courseService.listSelf(user_id));
    }

    @GetMapping("/userClass")
    public Result getUserClass(Integer course_id, Integer user_id)
    {
        return Result.ok(courseService.getUserClass(course_id,user_id));
    }

}
