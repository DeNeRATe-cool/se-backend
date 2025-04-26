package com.se.entity;

import lombok.Data;

@Data
public class Course {

    private Integer course_id;

    private String name;

    private int creator_id;

    private String syllabus;

    private String assMethod;

    private Float score;

    private Integer time;
}
