package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.entity.CandidateAssessment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResponseDTO {

    private UUID attemptId;

    // Assessment info
    private UUID assessmentId;
    private String assessmentTitle;
    private Integer timeValue;
    private String timeUnit;

    // Candidate info
    private UUID candidateId;
    private String candidateName;

    // Assignment info
    private UUID assignmentId;

    // Session info
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime expiresAt;
    private Double totalScore;
    private Double percentage;
    private CandidateAssessment.Status status;

    // Questions are included when starting so the candidate can answer them
    private List<QuestionDto> questions;
}
