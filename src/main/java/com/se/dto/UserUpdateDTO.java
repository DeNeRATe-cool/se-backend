package com.se.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Integer user_id;

    private String name;

    private String mail;

    private String birthday; // 字符串格式

}
