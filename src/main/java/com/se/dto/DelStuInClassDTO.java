package com.se.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelStuInClassDTO {

    @NotNull
    Integer course_id;

    @NotNull
    Integer class_id;

    @NotNull
    Integer user_id;

}
