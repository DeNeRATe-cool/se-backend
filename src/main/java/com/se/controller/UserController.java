package com.se.controller;

import com.se.dto.Result;
import com.se.entity.User;
import com.se.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    public Result info(@RequestParam Integer user_id) {
        User user = userService.info(user_id);
        return Result.ok(user);
    }

}
