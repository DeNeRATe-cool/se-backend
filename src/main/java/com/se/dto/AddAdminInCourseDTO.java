package com.se.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddAdminInCourseDTO {

    @NotBlank
    private String username;

    @NotNull
    private Integer course_id;

}
