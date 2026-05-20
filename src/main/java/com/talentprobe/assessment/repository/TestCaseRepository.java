package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestCaseRepository extends JpaRepository<TestCase, UUID> {
    List<TestCase> findByQuestion_QuestionId(UUID questionId);
}
