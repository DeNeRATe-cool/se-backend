package com.se.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Exercise {

    private Integer exer_id;

    private Integer class_id;

    private Integer course_id;

    private Integer creator_id;

    private Date begin_time;

    private Date end_time;

    private Boolean is_public;

    private String name;

    private Boolean is_multi;

    private Integer score;
}
