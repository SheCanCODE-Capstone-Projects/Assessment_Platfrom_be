package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.QuestionResultDTO;
import com.talentprobe.assessment.dto.ResultResponseDTO;
import com.talentprobe.assessment.email.EmailService;
import com.talentprobe.assessment.entity.*;
import com.talentprobe.assessment.enums.MarkingStatus;
import com.talentprobe.assessment.exception.ResourceNotFoundException;
import com.talentprobe.assessment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoringService {

    private final CandidateAssessmentRepository attemptRepository;
    private final SubmissionRepository submissionRepository;
    private final EmailService emailService;

    // ─── CALCULATE & GET RESULT FOR AN ATTEMPT ───────────────────────────────────
    // Called by admin after all submissions have been graded (scoreAwarded set).

    @Transactional
    public ResultResponseDTO getResult(UUID attemptId) {

        CandidateAssessment attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attempt not found with id: " + attemptId));

        if (attempt.getStatus() == CandidateAssessment.Status.STARTED) {
            throw new RuntimeException("Assessment has not been submitted yet");
        }

        // Get all graded submissions for this attempt
        List<Submission> submissions = submissionRepository
                .findByCandidateAssessment_AttemptId(attemptId);

        Assessment assessment = attempt.getAssessment();

        // ── Step 1: Calculate max score dynamically from assigned questions ──────
        int maxScore = calculateMaxScore(assessment);

        // ── Step 2: Sum all scoreAwarded set by admin ─────────────────────────────
        int totalScore = calculateTotalScore(submissions);

        // ── Step 3: Calculate percentage over 100 ────────────────────────────────
        double percentage = calculatePercentage(totalScore, maxScore);

        // ── Step 4: Determine PASS or FAIL ───────────────────────────────────────
        String result = determineResult(percentage, assessment.getPassMark());
        boolean passed = "PASS".equals(result);

        // ── Step 5: Update markingStatus on each submission ───────────────────────
        for (Submission submission : submissions) {
            MarkingStatus ms = submission.getScoreAwarded() > 0
                    ? MarkingStatus.PASSED
                    : MarkingStatus.FAILED;
            submission.setMarkingStatus(ms);
            submissionRepository.save(submission);
        }

        // ── Step 6: Sync scores + qualified flag onto the attempt ─────────────────
        attempt.setTotalScore((double) totalScore);
        attempt.setPercentage(percentage);
        attempt.setQualified(passed);
        attemptRepository.save(attempt);

        // ── Step 7: Build per-question breakdown ──────────────────────────────────
        List<QuestionResultDTO> questionResults = submissions.stream()
                .map(s -> QuestionResultDTO.builder()
                        .questionId(s.getQuestion().getQuestionId())
                        .questionTitle(s.getQuestion().getTitle())
                        .maxMarks(s.getQuestion().getMarks())
                        .earnedMarks(s.getScoreAwarded())
                        .adminReviewNote(s.getAdminReviewNote())
                        .markingStatus(s.getMarkingStatus())
                        .build())
                .collect(Collectors.toList());

        // ── Step 8: Send interview invitation email if PASSED ─────────────────────
        if (passed) {
            try {
                emailService.sendInterviewInvitation(
                        attempt.getCandidate().getEmail(),
                        attempt.getCandidate().getName(),
                        assessment.getExamTitle(),
                        percentage
                );
            } catch (Exception e) {
                System.err.println("Failed to send interview invitation: " + e.getMessage());
            }
        }

        return ResultResponseDTO.builder()
                .attemptId(attempt.getAttemptId())
                .candidateId(attempt.getCandidate().getUserId())
                .candidateName(attempt.getCandidate().getName())
                .candidateEmail(attempt.getCandidate().getEmail())
                .assessmentId(assessment.getAssessmentId())
                .assessmentTitle(assessment.getExamTitle())
                .totalScore(totalScore)
                .maxScore(maxScore)
                .percentage(percentage)
                .passMark(assessment.getPassMark())
                .result(result)
                .qualified(passed)
                .interviewDate(attempt.getInterviewDate())
                .questionResults(questionResults)
                .status(attempt.getStatus())
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .build();
    }

    // ─── SET INTERVIEW DATE (admin sets after candidate qualifies) ────────────────

    @Transactional
    public ResultResponseDTO setInterviewDate(UUID attemptId, LocalDateTime interviewDate) {

        CandidateAssessment attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attempt not found with id: " + attemptId));

        if (!Boolean.TRUE.equals(attempt.getQualified())) {
            throw new RuntimeException("Candidate has not qualified for an interview");
        }

        attempt.setInterviewDate(interviewDate);
        attemptRepository.save(attempt);

        // Re-send email with the interview date
        try {
            emailService.sendInterviewInvitationWithDate(
                    attempt.getCandidate().getEmail(),
                    attempt.getCandidate().getName(),
                    attempt.getAssessment().getExamTitle(),
                    attempt.getPercentage(),
                    interviewDate
            );
        } catch (Exception e) {
            System.err.println("Failed to send interview date email: " + e.getMessage());
        }

        return getResult(attemptId);
    }

    // ─── calculateTotalScore ──────────────────────────────────────────────────────
    // Sums all scoreAwarded values set by admin across all submissions

    public int calculateTotalScore(List<Submission> submissions) {
        return submissions.stream()
                .mapToInt(s -> s.getScoreAwarded() != null ? s.getScoreAwarded() : 0)
                .sum();
    }

    // ─── calculateMaxScore ────────────────────────────────────────────────────────
    // Dynamically sums marks from all questions assigned to the assessment

    public int calculateMaxScore(Assessment assessment) {
        return assessment.getQuestions()
                .stream()
                .mapToInt(Question::getMarks)
                .sum();
    }

    // ─── calculatePercentage ──────────────────────────────────────────────────────
    // (candidateTotalScore / assessmentTotalMarks) * 100

    public double calculatePercentage(int totalScore, int maxScore) {
        if (maxScore == 0) return 0.0;
        double raw = ((double) totalScore / maxScore) * 100.0;
        return Math.round(raw * 100.0) / 100.0;
    }

    // ─── determineResult ──────────────────────────────────────────────────────────
    // IF percentage >= passMark THEN PASS ELSE FAIL

    public String determineResult(double percentage, double passMark) {
        return percentage >= passMark ? "PASS" : "FAIL";
    }
}
