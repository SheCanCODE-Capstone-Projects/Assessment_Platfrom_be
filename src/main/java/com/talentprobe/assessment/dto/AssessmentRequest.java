package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.TimeUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssessmentRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer timeValue;

    @NotNull
    private TimeUnit timeUnit;

    @NotNull
    private Double passMark;
}
