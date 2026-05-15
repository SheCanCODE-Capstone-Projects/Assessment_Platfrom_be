package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.CandidateAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateAssessmentRepository extends JpaRepository<CandidateAssessment, UUID> {

    // Check if an attempt already exists for a given assignment (enforce one attempt per assignment)
    boolean existsByAssignmentAssignmentId(UUID assignmentId);

    // Find attempt by assignment id
    Optional<CandidateAssessment> findByAssignmentAssignmentId(UUID assignmentId);

    // Find all attempts for a candidate
    List<CandidateAssessment> findByCandidateUserId(UUID candidateId);

    // Find all started attempts that have passed their expiry time (for auto-submit)
    List<CandidateAssessment> findByStatusAndExpiresAtBefore(CandidateAssessment.Status status, LocalDateTime now);
}
