package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Problem;
import com.se.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    /**
     *
     * @param type
     * @return
     */
    @GetMapping("/public")
    public Result GetPublicProb(@RequestParam("types") Integer type){
        List<Problem> publicProb=problemService.PublicProb(type);
        return Result.ok(publicProb,publicProb.size());
    }
}
