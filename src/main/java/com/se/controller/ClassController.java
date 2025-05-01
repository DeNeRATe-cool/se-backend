package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Class;
import com.se.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping("/create")
    public Result add(@RequestBody Class classEntity)
    {
        classService.add(classEntity);
        return Result.ok(classEntity);
    }
}
