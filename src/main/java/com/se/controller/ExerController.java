package com.se.controller;


import com.se.dao.ExerDao;
import com.se.dto.*;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.service.ExerService;
import org.apache.commons.collections.ResettableListIterator;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    @GetMapping("/info")
    public Result info(@RequestParam Integer exer_id)
    {
        Exercise exercise = exerService.info(exer_id);
        return Result.ok(exercise);
    }

    @GetMapping("/get")
    public Result listProblemByExerId(@RequestParam Integer exer_id)
    {
        List<ProbInExer> res = exerService.listProblemByExerId(exer_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/class/get")
    public Result listExerByCourseAndClass(@RequestParam Integer course_id,
                                  @RequestParam Integer class_id)
    {
        List<Exercise> res = exerService.listExerByCourseAndClass(course_id,class_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/allSelf")
    public Result listExerByStuId(@RequestParam Integer user_id)
    {
        List<Exercise> exerciseList = exerService.listExerByStuId(user_id);
        return Result.ok(exerciseList,exerciseList.size());
    }

    @GetMapping("/alldone")
    public Result listDoneExerByStuId(@RequestParam Integer user_id)
    {
        List<Exercise> exerciseList = exerService.listDoneExerByStuId(user_id);
        return Result.ok(exerciseList,exerciseList.size());
    }

    @GetMapping("/allTodo")
    public Result listToDoExerByStuId(@RequestParam Integer user_id)
    {
        List<Exercise>exerciseList = exerService.listToDoExerByStuId(user_id);
        return Result.ok(exerciseList,exerciseList.size());
    }

    @GetMapping("/public")
    public Result listPublicExerByCourse(@RequestParam Integer course_id)
    {
        List<Exercise>exerciseList = exerService.listPublicExerByCourse(course_id);
        return Result.ok(exerciseList,exerciseList.size());
    }

    @GetMapping("/self")
    public Result listSelfCreateExer(@RequestParam Integer user_id)
    {
        List<Exercise> exerciseList = exerService.listSelfCreateExer(user_id);
        return Result.ok(exerciseList,exerciseList.size());
    }

    @PostMapping("/submit")
    public Result submit(@RequestBody SubmitExerciseDTO submitExerciseDTO)
    {
        Integer exer_id = submitExerciseDTO.getExer_id();
        Integer user_id = submitExerciseDTO.getUser_id();
        exerService.submit(exer_id,user_id);
        return Result.ok();
    }

    @PostMapping("/save")
    public Result save(@RequestBody SaveExerciseDTO saveExerciseDTO)
    {
        System.out.println(saveExerciseDTO.getAnslist());
        exerService.save(saveExerciseDTO.getExer_id(),
                saveExerciseDTO.getUser_id(),
                saveExerciseDTO.getAnslist());
        return Result.ok();
    }

    @GetMapping("/all/finish")
    public Result countFinsh(@RequestParam Integer exer_id)
    {
        Integer finishCount = exerService.countFinish(exer_id);
        return Result.ok(finishCount);
    }

    @GetMapping("/stuReport")
    public Result generateExerciseReport(
            @RequestParam("user_id") Integer userId) throws IOException {
        String url = exerService.generateExerciseReport(userId);
        return Result.ok(url);
    }

    @GetMapping("/adminReport")
    public Result generateFeedbackReport(
            @RequestParam("exer_id") Integer exerid) throws IOException {
        String url = exerService.generateFeedbackReport(exerid);
        return Result.ok(url);
    }

}
