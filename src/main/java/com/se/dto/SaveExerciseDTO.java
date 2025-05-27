package com.se.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveExerciseDTO {
    private Integer exer_id;
    private Integer user_id;
    private List<String> anslist;
}
