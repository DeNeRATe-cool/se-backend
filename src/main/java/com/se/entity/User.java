package com.se.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Integer user_id;

    private String name;

    private String username;

    private String password;

    private String mail;

    private Date birthday;

    private Integer identity;
}
