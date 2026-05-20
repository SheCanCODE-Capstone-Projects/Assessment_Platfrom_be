package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.SubmissionDto;
import com.talentprobe.assessment.entity.CodingSubmission;
import com.talentprobe.assessment.entity.Submission;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public SubmissionDto toDto(Submission submission, CodingSubmission codingSubmission) {
        SubmissionDto dto = new SubmissionDto();

        // Submission fields
        dto.setSubmissionId(submission.getSubmissionId());
        dto.setCandidateAssessmentId(submission.getCandidateAssessment().getAttemptId());
        dto.setQuestionId(submission.getQuestion().getQuestionId());
        dto.setQuestionTitle(submission.getQuestion().getTitle());
        dto.setScoreAwarded(submission.getScoreAwarded());
        dto.setTotalTestCases(submission.getTotalTestCases());
        dto.setPassedTestCases(submission.getPassedTestCases());
        dto.setAdminReviewNote(submission.getAdminReviewNote());
        dto.setAdminReviewed(submission.getAdminReviewed());
        dto.setMarkingStatus(submission.getMarkingStatus());
        dto.setSubmittedAt(submission.getSubmittedAt());

        // CodingSubmission fields
        if (codingSubmission != null) {
            dto.setCodingSubmissionId(codingSubmission.getCodingSubmissionId());
            dto.setSourceCode(codingSubmission.getSourceCode());
            dto.setLanguage(codingSubmission.getLanguage());
            dto.setGradingStatus(codingSubmission.getGradingStatus());
        }

        return dto;
    }
}
