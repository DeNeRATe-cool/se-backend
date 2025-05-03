package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Exercise;
import com.se.entity.User;
import com.se.service.ExerService;
import com.se.service.UserCourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/check")
public class CheckController {
    @Autowired
    private ExerService exerService;

    @GetMapping("/todo")
    public Result getNotCheckedExercise(
            @RequestParam("course_id") Integer courseId,
            @RequestParam("user_id") Integer userId) {
        List<Exercise> todoList = exerService.getNotCheckedExercise(courseId, userId);
        return Result.ok(todoList, todoList.size());
    }

    @GetMapping("stu")
    public Result getNotCheckedStu(
            @RequestParam("exer_id") Integer exerId) {
        List<User> stuList = exerService.getNotCheckedStu(exerId);
        return Result.ok(stuList, stuList.size());
    }
}
