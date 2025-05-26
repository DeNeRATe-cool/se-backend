package com.se.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Problem {

    private Integer prob_id;

    private Boolean is_public;

    private Integer type;

    private Integer creator_id;

    private Date create_time;

    private String description;

    private List<String> str_content;

    private String content;

    private String answer;

    private String analysis;
}
