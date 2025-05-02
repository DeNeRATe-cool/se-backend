package com.se.controller;


import com.se.dto.Result;
import com.se.service.ExerService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
