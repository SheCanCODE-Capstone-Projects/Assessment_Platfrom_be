package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.CodingSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CodingSubmissionRepository extends JpaRepository<CodingSubmission, UUID> {

    Optional<CodingSubmission> findBySubmission_SubmissionId(UUID submissionId);
}
