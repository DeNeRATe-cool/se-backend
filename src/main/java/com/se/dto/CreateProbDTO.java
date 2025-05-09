package com.se.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateProbDTO {
    private Integer prob_id;
    @NotNull
    private Integer type;
    @NotNull
    private Boolean is_public;
    @NotNull
    private Integer creator_id;
    @NotNull
    private String description;

    private List<String> content;
    @NotNull
    private String answer;

    private String analysis;
    private String str_content;
}
