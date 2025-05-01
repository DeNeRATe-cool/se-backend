package com.se.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class Class {

    private Integer class_id;
    @NotBlank
    private String name;
    @NotEmpty
    private Integer course_id;

    private String class_code;
}
