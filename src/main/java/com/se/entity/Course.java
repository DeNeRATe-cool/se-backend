package com.se.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Course {
    private Integer course_id;
    @NotEmpty
    private String name;
    @NotEmpty
    private int creator_id;

    private String syllabus;
    @NotEmpty
    private String assMethod;
    @NotEmpty
    private Float score;
    @NotEmpty
    private Integer time;
}
