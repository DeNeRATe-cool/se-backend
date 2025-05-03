package com.se.controller;

import com.se.dao.ProbDao;
import com.se.dao.StuProbExerDao;
import com.se.dto.Result;
import com.se.dto.StuProbExer;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.entity.User;
import com.se.service.ExerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/check")
public class CheckController {
    @Autowired
    private ExerService exerService;

    @Autowired
    private StuProbExerDao stuProbExerDao;

    @Autowired
    private ProbDao probDao;

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

    @GetMapping("get")
    public Result getCheckInfo(
            @RequestParam("exer_id") Integer exerId,
            @RequestParam("user_id") Integer userId) {
        // 题目任务学生，标准得分，题目序号
        List<StuProbExer> baseList = stuProbExerDao.getProblemListByExerID(exerId);
        Collections.sort(baseList, Comparator.comparing(StuProbExer::getIdx).reversed());

        // 题目信息
        List<Problem> proList = new ArrayList<>();
        for(StuProbExer stuProbExer : baseList)
            proList.add(probDao.getProblemById(stuProbExer.getProb_id()));

        // 用户数据
        List<StuProbExer> stuExerList = new ArrayList<>();
        for(StuProbExer stuProbExer : baseList)
            stuExerList.add(stuProbExerDao
                    .getInfoByUserIDAndProbIDAndExerID(userId, stuProbExer.getProb_id(), exerId));

        /* 已按照题目序号处理好 */
        List<List<?>> infoList = exerService.getCheckInfo(baseList, proList, stuExerList, userId);
        return Result.ok(infoList, infoList.size());
    }
}
