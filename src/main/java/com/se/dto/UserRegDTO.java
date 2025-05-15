package com.se.dto;

import lombok.Data;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UserRegDTO {
    private Integer user_id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String mail;

    private String birthday;

    private Integer identity;

    @NotNull
    private Integer verifyCode;
}
