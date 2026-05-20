package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.GradeSubmissionRequest;
import com.talentprobe.assessment.dto.SubmissionDto;
import com.talentprobe.assessment.dto.SubmissionRequest;
import com.talentprobe.assessment.entity.CandidateAssessment;
import com.talentprobe.assessment.entity.CodingSubmission;
import com.talentprobe.assessment.entity.Question;
import com.talentprobe.assessment.entity.Submission;
import com.talentprobe.assessment.enums.GradingStatus;
import com.talentprobe.assessment.exception.DuplicateResourceException;
import com.talentprobe.assessment.exception.ResourceNotFoundException;
import com.talentprobe.assessment.mapper.SubmissionMapper;
import com.talentprobe.assessment.repository.CandidateAssessmentRepository;
import com.talentprobe.assessment.repository.CodingSubmissionRepository;
import com.talentprobe.assessment.repository.QuestionRepository;
import com.talentprobe.assessment.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final CodingSubmissionRepository codingSubmissionRepository;
    private final QuestionRepository questionRepository;
    private final CandidateAssessmentRepository candidateAssessmentRepository;
    private final SubmissionMapper submissionMapper;

    /**
     * submitCode() — candidate submits code for a question.
     * Creates a Submission (score record) + CodingSubmission (code record).
     * One submission per question per attempt.
     */
    @Transactional
    public SubmissionDto submitCode(SubmissionRequest request) {
        CandidateAssessment candidateAssessment = candidateAssessmentRepository
                .findById(request.getCandidateAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assessment attempt not found with id: " + request.getCandidateAssessmentId()));

        if (candidateAssessment.getStatus() != CandidateAssessment.Status.STARTED) {
            throw new IllegalStateException(
                    "Cannot submit — this assessment has already been completed or auto-submitted");
        }

        if (submissionRepository.existsByCandidateAssessment_AttemptIdAndQuestion_QuestionId(
                request.getCandidateAssessmentId(), request.getQuestionId())) {
            throw new DuplicateResourceException(
                    "You have already submitted code for this question");
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question not found with id: " + request.getQuestionId()));

        // Save Submission (score record)
        Submission submission = Submission.builder()
                .candidateAssessment(candidateAssessment)
                .question(question)
                .scoreAwarded(0)
                .adminReviewed(false)
                .build();
        submission = submissionRepository.save(submission);

        // Save CodingSubmission (code record)
        CodingSubmission codingSubmission = CodingSubmission.builder()
                .submission(submission)
                .sourceCode(request.getSourceCode())
                .language(request.getLanguage())
                .gradingStatus(GradingStatus.PENDING)
                .build();
        codingSubmission = codingSubmissionRepository.save(codingSubmission);

        return submissionMapper.toDto(submission, codingSubmission);
    }

    /**
     * getSubmissionsByAttempt() — returns all submissions for a test attempt.
     */
    public List<SubmissionDto> getSubmissionsByAttempt(UUID candidateAssessmentId) {
        return submissionRepository
                .findByCandidateAssessment_AttemptId(candidateAssessmentId)
                .stream()
                .map(submission -> {
                    CodingSubmission coding = codingSubmissionRepository
                            .findBySubmission_SubmissionId(submission.getSubmissionId())
                            .orElse(null);
                    return submissionMapper.toDto(submission, coding);
                })
                .collect(Collectors.toList());
    }

    /**
     * Get a single submission by ID.
     */
    public SubmissionDto getSubmissionById(UUID submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Submission not found with id: " + submissionId));

        CodingSubmission coding = codingSubmissionRepository
                .findBySubmission_SubmissionId(submissionId)
                .orElse(null);

        return submissionMapper.toDto(submission, coding);
    }

    /**
     * saveSubmission() — admin grades a submission.
     * Sets score_awarded, admin_review_note, marks grading_status as REVIEWED.
     */
    @Transactional
    public SubmissionDto saveSubmission(UUID submissionId, GradeSubmissionRequest request) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Submission not found with id: " + submissionId));

        int maxMarks = submission.getQuestion().getMarks();
        if (request.getScoreAwarded() > maxMarks) {
            throw new IllegalArgumentException(
                    "Score cannot exceed question marks (" + maxMarks + ")");
        }

        submission.setScoreAwarded(request.getScoreAwarded());
        submission.setAdminReviewNote(request.getAdminReviewNote());
        submission.setAdminReviewed(true);

        // Set markingStatus based on whether any marks were awarded
        submission.setMarkingStatus(
            request.getScoreAwarded() > 0
                ? com.talentprobe.assessment.enums.MarkingStatus.PASSED
                : com.talentprobe.assessment.enums.MarkingStatus.FAILED
        );

        submission = submissionRepository.save(submission);

        // Mark coding submission as REVIEWED
        CodingSubmission coding = codingSubmissionRepository
                .findBySubmission_SubmissionId(submissionId)
                .orElse(null);

        if (coding != null) {
            coding.setGradingStatus(GradingStatus.REVIEWED);
            coding = codingSubmissionRepository.save(coding);
        }

        return submissionMapper.toDto(submission, coding);
    }
}
