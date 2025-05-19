package com.se.controller;


import com.se.dto.Result;
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

    @GetMapping("/all")
    public Result listByCourse(int course_id)
    {
        List<User> res =  teacherService.listByCourse(course_id);
        Map<String,Object> map = ThreadLocalUtil.get();
        return Result.ok(res,res.size());
    }

    @GetMapping("exact")
    public Result listByCondition(@RequestParam Map<String,String> params)
    {
        List<User> res = teacherService.listByCondition(params);
        return Result.ok(res,res.size());
    }

}
