package com.se.controller;


import com.se.dao.ExerDao;
import com.se.dto.CreateExerDTO;
import com.se.dto.PushExerDTO;
import com.se.dto.Result;
import com.se.entity.Exercise;
import com.se.service.ExerService;
import org.apache.commons.collections.ResettableListIterator;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/*
    处理练习相关接口

 */
@RestController
@RequestMapping("/exer")
public class ExerController {

    @Autowired
    private ExerService exerService;

    @Autowired
    private ExerDao exerDao;

    /**
     * 功能： 学生查询对应班级 已完成任务
     *
     * @param
     *
     */
    @GetMapping("/stu/finish")
    public Result getStuFinishExerNum(Integer user_id,Integer course_id,Integer class_id)
    {
        List<Integer> ls = exerService.getStuFinishExerNum(user_id,course_id,class_id);
        return Result.ok(ls,ls.size());
    }


    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateExerDTO createExerDTO)
    {
        Exercise exercise = exerService.create(createExerDTO);
        return Result.ok(exercise);
    }

    @PostMapping("/push")
    public Result push(@RequestBody @Validated PushExerDTO pushExerDTO)
    {
        exerService.push(pushExerDTO);
        return Result.ok();
    }

    @PostMapping("/info")
    public Result info(@RequestParam Integer exer_id)
    {
        Exercise exercise = exerService.info(exer_id);
        return Result.ok(exercise);
    }
}
