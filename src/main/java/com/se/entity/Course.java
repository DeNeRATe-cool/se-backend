package com.se.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Course {
    private Integer course_id;

    @NotEmpty
    private String name;

    @NotNull
    private int creator_id;

    private String syllabus;

    @NotEmpty
    private String assMethod;

    @NotNull
    private Float score;

    @NotNull
    private Integer time;
}
