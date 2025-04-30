package com.se.dto;

import lombok.Data;

@Data
public class StuProbExer {
    private Integer stu_id;
    private Integer prob_id;
    private Integer exer_id;
    private Integer score;
    private String comment;
    private String submit;
    private Boolean is_finish;
    private Boolean is_check;
    private Integer idx;
}
