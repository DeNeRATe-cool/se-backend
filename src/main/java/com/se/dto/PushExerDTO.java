package com.se.dto;


import cn.hutool.core.date.DateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PushExerDTO {

    @NotNull
    private Integer exer_id;

    @NotNull
    private Integer course_id;

    @NotNull
    private Integer class_id;

    @NotNull
    private Integer creator_id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime begin_time;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime end_time;

    @NotNull
    private Boolean is_multi;

    @NotBlank
    private String name;

    @NotNull
    private Boolean is_every_class;

}
