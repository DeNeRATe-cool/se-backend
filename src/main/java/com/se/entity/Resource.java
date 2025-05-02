package com.se.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.util.Date;

@Data
public class Resource {
    private Integer res_id;

    private String res_code;

    private String name;

    private Integer process_id;

    private Integer course_id;

    private Integer class_id;

    private String type;

    private String url;

    private Date date;

    private String tag;
}
