package com.se.controller;


import com.github.pagehelper.PageInfo;
import com.se.dto.Result;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.service.TeacherService;
import com.se.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/allteacher")
    public Result list()
    {
        List<User>res = teacherService.list();
        return Result.ok(res,res.size());
    }

    @GetMapping("/allteacher/page")
    public Result listPage(Integer page, Integer size)
    {
        return Result.ok(teacherService.listPage(page,size));
    }

    @GetMapping("/all")
    public Result listByCourse(int course_id)
    {
        List<User> res =  teacherService.listByCourse(course_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/all/page")
    public Result listByCourse(int course_id,int page,int size)
    {
        PageInfo<Course> res =  teacherService.listByCoursePage(course_id,page,size);
        return Result.ok(res);
    }

    @GetMapping("exact")
    public Result listByCondition(@RequestParam Map<String,String> params)
    {
        List<User> res = teacherService.listByCondition(params);
        return Result.ok(res,res.size());
    }

}
