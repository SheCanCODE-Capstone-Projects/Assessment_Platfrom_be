package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.TimeUnit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AssessmentDto {
    private UUID assessmentId;
    private String title;
    private String description;
    private Integer timeValue;
    private TimeUnit timeUnit;
    private Double passMark;
    private LocalDateTime createdAt;
}
