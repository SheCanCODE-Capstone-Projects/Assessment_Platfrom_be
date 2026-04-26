package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByAssessment_AssessmentId(UUID assessmentId);
}
