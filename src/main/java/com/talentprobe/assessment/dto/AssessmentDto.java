package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.AssessmentStatus;
import com.talentprobe.assessment.enums.TimeUnit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class AssessmentDto {
    private UUID assessmentId;
    private String examTitle;
    private String description;
    private Integer timeValue;
    private TimeUnit timeUnit;
    private Double passMark;
    private AssessmentStatus status;
    private List<QuestionDto> questions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
