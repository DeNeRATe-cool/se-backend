package com.se.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer user_id;

    private String name;

    private String username;

    private String password;

    private String mail;

    private Date birthday;

    private Integer identity;
}
