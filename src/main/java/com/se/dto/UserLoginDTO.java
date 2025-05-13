package com.se.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class UserLoginDTO {
    @NotEmpty
    private String account;
    @NotEmpty
    private String password;
}
