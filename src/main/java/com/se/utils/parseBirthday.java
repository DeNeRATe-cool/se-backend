package com.se.utils;

import com.se.exception.userException.UserBizException;
import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class parseBirthday {
    public static Date parseBirthday(String birthdayStr){
        if(!StringUtils.hasText(birthdayStr)){
            return Date.valueOf("2000-01-01");
        }
        try{
            LocalDate localDate=LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return Date.valueOf(localDate);
        }catch (DateTimeParseException e){
            throw new UserBizException("生日格式不合法，应为 yyyy-MM-dd");
        }
    }
}
