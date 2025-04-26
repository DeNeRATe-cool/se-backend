package com.se.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

@Data
public class Exercise {

    private Integer exer_id;

    private Integer class_id;

    private Integer course_id;

    private Integer creator_id;

    private DateTime begin_time;

    private DateTime end_time;

    private Boolean is_public;

    private String name;

    private Boolean is_multi;

    private Integer score;
}
