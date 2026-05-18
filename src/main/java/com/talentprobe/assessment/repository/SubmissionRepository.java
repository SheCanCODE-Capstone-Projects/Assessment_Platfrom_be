package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    List<Submission> findByCandidateAssessment_AttemptId(UUID candidateAssessmentId);

    boolean existsByCandidateAssessment_AttemptIdAndQuestion_QuestionId(UUID candidateAssessmentId, UUID questionId);
}
