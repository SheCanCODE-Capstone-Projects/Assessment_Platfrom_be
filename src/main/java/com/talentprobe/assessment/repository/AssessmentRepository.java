package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
}
