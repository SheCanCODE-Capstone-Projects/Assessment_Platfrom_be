package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SubmissionRequest {

    @NotNull(message = "candidateAssessmentId is required")
    private UUID candidateAssessmentId;

    @NotNull(message = "questionId is required")
    private UUID questionId;

    @NotBlank(message = "sourceCode is required")
    private String sourceCode;

    @NotNull(message = "language is required")
    private Language language;
}
