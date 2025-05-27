package com.se.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProbInExer {
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

    private Integer score;
}
