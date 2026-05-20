package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.GradingStatus;
import com.talentprobe.assessment.enums.Language;
import com.talentprobe.assessment.enums.MarkingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubmissionDto {

    // Submission fields
    private UUID submissionId;
    private UUID candidateAssessmentId;
    private UUID questionId;
    private String questionTitle;
    private Integer scoreAwarded;
    private Integer totalTestCases;
    private Integer passedTestCases;
    private String adminReviewNote;
    private Boolean adminReviewed;
    private MarkingStatus markingStatus;
    private LocalDateTime submittedAt;

    // CodingSubmission fields
    private UUID codingSubmissionId;
    private String sourceCode;
    private Language language;
    private GradingStatus gradingStatus;
}
