package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.AssessmentStatus;
import com.talentprobe.assessment.enums.TimeUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AssessmentRequest {

    @NotBlank
    private String examTitle;

    @NotBlank
    private String description;

    @NotNull
    private Integer timeValue;

    @NotNull
    private TimeUnit timeUnit;

    @NotNull
    private Double passMark;

    private AssessmentStatus status;

    private List<UUID> questionIds;
}
