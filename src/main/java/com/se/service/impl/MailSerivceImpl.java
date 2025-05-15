package com.se.service.impl;

import com.se.dto.Result;
import com.se.exception.userException.UserBizException;
import com.se.service.MailService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailSerivceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public static final Map<String,Integer> VERIFY_CODE_CACHE = new ConcurrentHashMap<>();

    @Override
    public Result sendVerifyCode(String mail) {
        if(!mail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new UserBizException("邮箱格式错误");
        }
        int code = new Random().nextInt(900000)+100000;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mail);
        message.setSubject("软平台注册验证码");
        message.setText("您好，您的验证码为："+code+"，5分钟有效，请勿泄露");
        try{
            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
            throw new UserBizException("验证码发送失败："+e.getMessage());
        }
        VERIFY_CODE_CACHE.put(mail,code);
        return Result.ok("验证码已发送到您的邮箱："+mail+"，请查收");
    }
}
