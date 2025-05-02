package com.se.controller;

import com.se.dto.AddAdminDTO;
import com.se.dto.Result;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result addAdmin(@RequestBody @Validated AddAdminDTO addAdminDTO)
    {
        List<User>res = courseService.addAdmin(addAdminDTO);
        return Result.ok(res);
    }


}
