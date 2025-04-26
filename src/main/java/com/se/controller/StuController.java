package com.se.controller;

import com.se.dto.Result;
import com.se.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stu")
public class StuController {
    @Autowired
    private StuService stuService;

    @GetMapping("/allStu")
    public Result getAllStudent() {
        return stuService.getAllStudent();
    }
}
