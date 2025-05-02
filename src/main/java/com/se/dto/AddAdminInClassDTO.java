package com.se.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddAdminInClassDTO {

    @NotNull
    private Integer class_id;

    @NotNull
    private Integer user_id;

    @NotNull
    private Integer course_id;

}
