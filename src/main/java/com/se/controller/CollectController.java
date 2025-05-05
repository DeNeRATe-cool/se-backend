package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Problem;
import com.se.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    @PostMapping("/addLike")
    public Result addLike(
            @RequestParam("prob_id") Integer probId,
            @RequestParam("stu_id") Integer userId) {
        collectService.addLike(probId, userId);
        return Result.ok();
    }

    @PostMapping("/addWrong")
    public Result addLike(
            @RequestParam("prob_id") Integer probId,
            @RequestParam("exer_id") Integer exerId,
            @RequestParam("stu_id") Integer userId) {
        collectService.addWrong(probId, exerId, userId);
        return Result.ok();
    }

    @DeleteMapping("/deleteLike")
    public Result deleteLike(
            @RequestParam("prob_id") Integer probId,
            @RequestParam("stu_id") Integer userId) {
        collectService.deleteLike(probId, userId);
        return Result.ok();
    }

    @GetMapping("/getWrong")
    public Result getWrong(
            @RequestParam("stu_id") Integer userId) {
        List<List<?>> resList = collectService.getWrong(userId);
        return Result.ok(resList, resList.size());
    }

    @GetMapping("/getLike")
    public Result getLike(
            @RequestParam("stu_id") Integer userId) {
        List<Problem> probList = collectService.getLike(userId);
        return Result.ok(probList, probList.size());
    }
}
