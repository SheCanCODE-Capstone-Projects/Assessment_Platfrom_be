package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.AssessmentDto;
import com.talentprobe.assessment.dto.AssessmentRequest;
import com.talentprobe.assessment.entity.Assessment;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper {

    public AssessmentDto toDto(Assessment assessment) {
        AssessmentDto dto = new AssessmentDto();
        dto.setAssessmentId(assessment.getAssessmentId());
        dto.setTitle(assessment.getTitle());
        dto.setDescription(assessment.getDescription());
        dto.setTimeValue(assessment.getTimeValue());
        dto.setTimeUnit(assessment.getTimeUnit());
        dto.setPassMark(assessment.getPassMark());
        dto.setCreatedAt(assessment.getCreatedAt());
        return dto;
    }

    public Assessment toEntity(AssessmentRequest request) {
        return Assessment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .timeValue(request.getTimeValue())
                .timeUnit(request.getTimeUnit())
                .passMark(request.getPassMark())
                .build();
    }
}
