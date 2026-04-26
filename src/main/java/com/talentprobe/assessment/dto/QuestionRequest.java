package com.talentprobe.assessment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class QuestionRequest {

    @NotBlank
    private String promptText;

    @NotNull
    private Integer defaultMarks;

    @NotNull
    private UUID assessmentId;
}
