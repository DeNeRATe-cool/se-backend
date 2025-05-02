package com.se.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ApplyJoinClassDTO {

    @NotNull
    private Integer course_id;

    @NotBlank
    private String class_code;

    @NotNull
    private Integer user_id;

}
