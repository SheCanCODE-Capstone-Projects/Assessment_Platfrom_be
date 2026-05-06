package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.CandidateAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface AssignmentRepository extends JpaRepository<CandidateAssignment, UUID> {
    // Find assignment by secure token
    Optional<CandidateAssignment> findBySecureToken(String secureToken);

    // Find all assignments for a specific candidate
    List<CandidateAssignment> findByCandidateUserId(UUID candidateId);

    // Find all assignments for a specific assessment
    List<CandidateAssignment> findByAssessmentAssessmentId(UUID assessmentId);

    // Find pending assignments (not yet started + link not expired)
    List<CandidateAssignment> findByLinkExpiryAfter(LocalDateTime now);
}
