package com.talentprobe.assessment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GradeSubmissionRequest {

    @NotNull(message = "scoreAwarded is required")
    @Min(value = 0, message = "Score cannot be negative")
    private Integer scoreAwarded;

    private String adminReviewNote;
}
