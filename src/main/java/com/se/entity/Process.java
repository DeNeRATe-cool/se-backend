package com.se.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Process {

    private Integer process_id;

    private String name;

    private Integer course_id;

    private Integer class_id;

    private Date time;

}
