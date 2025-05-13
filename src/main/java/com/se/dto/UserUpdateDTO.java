package com.se.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String mail;
    private String birthday; // 字符串格式
    private Integer identity;
}
