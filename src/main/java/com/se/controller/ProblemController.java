package com.se.controller;

import com.se.dto.CreateProbDTO;
import com.se.dto.Result;
import com.se.entity.Problem;
import com.se.service.ProblemService;
import org.apache.ibatis.ognl.ListPropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @GetMapping("info")
    public Result GetProbInfo(@RequestParam(value="id") Integer prob_id){
        Problem problem=problemService.getProb(prob_id);
        return Result.ok(problem);
    }
    @GetMapping("/public")
    public Result GetPublicProb(@RequestParam(value = "types",required = false) Integer type){
        List<Problem> publicProb=problemService.PublicProb(type);
        return Result.ok(publicProb,publicProb.size());
    }
    @PostMapping("/add")
    public Result CreateProb(@RequestBody Problem problem){
        Problem prob=problemService.createProb(problem);
        return Result.ok(prob);
    }

    @GetMapping("/self")
    public Result GetselfProb(@RequestParam("user_id") Integer userid,@RequestParam(value="types",required = false) Integer types){
        List<Problem> selfProb=problemService.SelfProb(userid,types);
        return Result.ok(selfProb,selfProb.size());
    }
}
