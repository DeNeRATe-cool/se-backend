package com.se.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SubmitExerciseDTO {

    @NotNull
    Integer exer_id;
    @NotNull
    Integer user_id;
}
