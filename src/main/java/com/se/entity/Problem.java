package com.se.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;

@Data
public class Problem {

    private Integer prob_id;

    private Boolean is_public;

    private Integer type;

    private Integer creator_id;

    private DateTime creation_time;

    private String description;

    private String content;

    private String answer;

    private String analysis;
}
