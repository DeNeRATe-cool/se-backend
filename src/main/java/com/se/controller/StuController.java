package com.se.controller;

import com.se.dto.Result;
import com.se.entity.User;
import com.se.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stu")
public class StuController {
    @Autowired
    private StuService stuService;

    @GetMapping("/allStu")
    public Result getAllStudent() {
        return stuService.getAllStudent();
    }

    @GetMapping("/all")
    public Result listByCourseID(Integer course_id)
    {
        List<User> res = stuService.listByCourseID(course_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/exact")
    public Result listByCondition(@RequestParam Map<String,String> params)
    {
        List<User> res = stuService.listByCondition(params);
        return Result.ok(res,res.size());
    }
}
