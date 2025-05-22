package com.se.controller;

import com.se.dto.Result;
import com.se.dto.UserLoginDTO;
import com.se.dto.UserRegDTO;
import com.se.dto.UserUpdateDTO;
import com.se.entity.User;
import com.se.service.MailService;
import com.se.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @PostMapping("/modify")
    public Result modifyUser(@RequestBody UserUpdateDTO dto){
        return userService.modify(dto);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO dto){
        return userService.login(dto);
    }

    @GetMapping("/verify")
    public Result sendVerifyCode(@RequestParam String mail){
        return mailService.sendVerifyCode(mail);
    }

    @PostMapping("/reg")
    public Result register(@RequestBody UserRegDTO userRegDTO){
        User user = userService.register(userRegDTO);
        return Result.ok(user);
    }

    @RequestMapping("/info")
    public Result info(@RequestParam Integer user_id) {
        User user = userService.info(user_id);
        return Result.ok(user);
    }

//    @GetMapping("/info")暂且先废弃，看前端后续需要
//    public Result getUserInfo(HttpServletRequest request) {
//        Integer userId = Integer.valueOf((String) request.getAttribute("userId"));
//        User user = userService.info(userId);
//        return Result.ok(user);
//    }

}
