package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.entity.CandidateAssessment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponseDTO {

    private UUID attemptId;

    // Candidate info
    private UUID candidateId;
    private String candidateName;
    private String candidateEmail;

    // Assessment info
    private UUID assessmentId;
    private String assessmentTitle;

    // Scoring
    private Integer totalScore;
    private Integer maxScore;
    private Double percentage;
    private Double passMark;
    private String result;          // "PASS" or "FAIL"
    private Boolean qualified;      // true if passed and invited to interview

    // Interview
    private LocalDateTime interviewDate;

    // Per-question breakdown
    private List<QuestionResultDTO> questionResults;

    // Attempt metadata
    private CandidateAssessment.Status status;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
}
