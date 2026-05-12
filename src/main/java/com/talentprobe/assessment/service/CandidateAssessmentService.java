package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.AttemptResponseDTO;
import com.talentprobe.assessment.dto.QuestionDto;
import com.talentprobe.assessment.entity.Assessment;
import com.talentprobe.assessment.entity.CandidateAssessment;
import com.talentprobe.assessment.entity.CandidateAssignment;
import com.talentprobe.assessment.exception.DuplicateResourceException;
import com.talentprobe.assessment.exception.ResourceNotFoundException;
import com.talentprobe.assessment.repository.AssignmentRepository;
import com.talentprobe.assessment.repository.CandidateAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateAssessmentService {

    @Autowired
    private CandidateAssessmentRepository attemptRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    // ─── START ASSESSMENT ────────────────────────────────────────────────────────

    public AttemptResponseDTO startAssessment(String token) {

        // 1. Find the assignment by token
        CandidateAssignment assignment = assignmentRepository
                .findBySecureToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        // 2. Check token is not expired
        if (assignment.getLinkExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("This assessment link has expired");
        }

        // 3. Enforce one attempt per assignment
        if (attemptRepository.existsByAssignmentAssignmentId(assignment.getAssignmentId())) {
            CandidateAssessment existing = attemptRepository
                    .findByAssignmentAssignmentId(assignment.getAssignmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Attempt not found"));
            return toResponseDTO(existing, true);
        }

        // 4. Calculate expiry time from assessment time limit
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = calculateExpiresAt(now, assignment.getAssessment());

        // 5. Create new attempt
        CandidateAssessment attempt = CandidateAssessment.builder()
                .assessment(assignment.getAssessment())
                .candidate(assignment.getCandidate())
                .assignment(assignment)
                .status(CandidateAssessment.Status.STARTED)
                .expiresAt(expiresAt)
                .build();

        CandidateAssessment saved = attemptRepository.save(attempt);

        return toResponseDTO(saved, true);
    }

    // ─── SUBMIT ASSESSMENT ───────────────────────────────────────────────────────

    public AttemptResponseDTO submitAssessment(UUID attemptId) {

        CandidateAssessment attempt = attemptRepository
                .findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        // Prevent double submission
        if (attempt.getStatus() != CandidateAssessment.Status.STARTED) {
            throw new DuplicateResourceException("Assessment has already been submitted");
        }

        attempt.setSubmittedAt(LocalDateTime.now());

        // If time already expired, mark as AUTO_SUBMITTED
        if (attempt.getExpiresAt() != null && attempt.getSubmittedAt().isAfter(attempt.getExpiresAt())) {
            attempt.setStatus(CandidateAssessment.Status.AUTO_SUBMITTED);
        } else {
            attempt.setStatus(CandidateAssessment.Status.COMPLETED);
        }

        CandidateAssessment saved = attemptRepository.save(attempt);

        return toResponseDTO(saved, false);
    }

    // ─── AUTO SUBMIT EXPIRED ATTEMPTS (called by scheduler) ──────────────────────

    public void autoSubmitExpiredAttempts() {
        List<CandidateAssessment> expired = attemptRepository
                .findByStatusAndExpiresAtBefore(CandidateAssessment.Status.STARTED, LocalDateTime.now());

        for (CandidateAssessment attempt : expired) {
            attempt.setSubmittedAt(LocalDateTime.now());
            attempt.setStatus(CandidateAssessment.Status.AUTO_SUBMITTED);
            attemptRepository.save(attempt);
        }
    }

    // ─── GET ATTEMPT BY ID ───────────────────────────────────────────────────────

    public AttemptResponseDTO getAttemptById(UUID attemptId) {
        CandidateAssessment attempt = attemptRepository
                .findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        return toResponseDTO(attempt, false);
    }

    // ─── UPDATE ATTEMPT SCORE (used by Person 6) ─────────────────────────────────

    public AttemptResponseDTO updateAttemptScore(UUID attemptId, Double totalScore, Double percentage) {
        CandidateAssessment attempt = attemptRepository
                .findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        attempt.setTotalScore(totalScore);
        attempt.setPercentage(percentage);

        CandidateAssessment saved = attemptRepository.save(attempt);

        return toResponseDTO(saved, false);
    }

    // ─── GET ALL ATTEMPTS FOR A CANDIDATE ────────────────────────────────────────

    public List<AttemptResponseDTO> getAttemptsByCandidate(UUID candidateId) {
        return attemptRepository.findByCandidateUserId(candidateId)
                .stream()
                .map(a -> toResponseDTO(a, false))
                .collect(Collectors.toList());
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────────

    private LocalDateTime calculateExpiresAt(LocalDateTime startedAt, Assessment assessment) {
        int value = assessment.getTimeValue();
        return switch (assessment.getTimeUnit()) {
            case SECONDS -> startedAt.plusSeconds(value);
            case MINUTES -> startedAt.plusMinutes(value);
            case HOURS   -> startedAt.plusHours(value);
        };
    }

    // ─── MAPPER ──────────────────────────────────────────────────────────────────

    private AttemptResponseDTO toResponseDTO(CandidateAssessment attempt, boolean includeQuestions) {

        AttemptResponseDTO.AttemptResponseDTOBuilder builder = AttemptResponseDTO.builder()
                .attemptId(attempt.getAttemptId())
                .assessmentId(attempt.getAssessment().getAssessmentId())
                .assessmentTitle(attempt.getAssessment().getExamTitle())
                .timeValue(attempt.getAssessment().getTimeValue())
                .timeUnit(attempt.getAssessment().getTimeUnit().name())
                .candidateId(attempt.getCandidate().getUserId())
                .candidateName(attempt.getCandidate().getName())
                .assignmentId(attempt.getAssignment().getAssignmentId())
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .expiresAt(attempt.getExpiresAt())
                .totalScore(attempt.getTotalScore())
                .percentage(attempt.getPercentage())
                .status(attempt.getStatus());

        if (includeQuestions) {
            List<QuestionDto> questions = attempt.getAssessment().getQuestions()
                    .stream()
                    .map(q -> {
                        QuestionDto dto = new QuestionDto();
                        dto.setQuestionId(q.getQuestionId());
                        dto.setTitle(q.getTitle());
                        dto.setDescription(q.getDescription());
                        dto.setMarks(q.getMarks());
                        dto.setDifficulty(q.getDifficulty());
                        dto.setLanguage(q.getLanguage());
                        dto.setStarterCode(q.getStarterCode());
                        return dto;
                    })
                    .collect(Collectors.toList());
            builder.questions(questions);
        }

        return builder.build();
    }
}
