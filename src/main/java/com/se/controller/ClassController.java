package com.se.controller;

import com.se.dto.AddAdminInClassDTO;
import com.se.dto.ApplyJoinClassDTO;
import com.se.dto.Result;
import com.se.entity.Class;
import com.se.entity.User;
import com.se.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/addAdmin")
    public Result addAdmin(@RequestBody AddAdminInClassDTO addAdminInClassDTO)
    {
        List<User> res = classService.addAdmin(addAdminInClassDTO);
        return Result.ok(res,res.size());
    }

    @GetMapping("/admins")
    public Result listTeacherAndTutor(Integer class_id)
    {
        List<User> res = classService.listTeacherAndTutor(class_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/all")
    public Result list()
    {
        List<Class> res = classService.list();
        return Result.ok(res,res.size());
    }

    @PostMapping("/apply")
    public Result apply(@RequestBody @Validated ApplyJoinClassDTO applyJoinClassDTO)
    {
        classService.apply(applyJoinClassDTO);
        return Result.ok();
    }
}
