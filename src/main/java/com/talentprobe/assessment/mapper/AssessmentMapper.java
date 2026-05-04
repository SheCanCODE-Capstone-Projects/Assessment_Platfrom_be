package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.AssessmentDto;
import com.talentprobe.assessment.dto.AssessmentRequest;
import com.talentprobe.assessment.entity.Assessment;
import com.talentprobe.assessment.enums.AssessmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssessmentMapper {

    private final QuestionMapper questionMapper;

    public AssessmentDto toDto(Assessment assessment) {
        AssessmentDto dto = new AssessmentDto();
        dto.setAssessmentId(assessment.getAssessmentId());
        dto.setExamTitle(assessment.getExamTitle());
        dto.setDescription(assessment.getDescription());
        dto.setTimeValue(assessment.getTimeValue());
        dto.setTimeUnit(assessment.getTimeUnit());
        dto.setPassMark(assessment.getPassMark());
        dto.setStatus(assessment.getStatus());
        dto.setCreatedAt(assessment.getCreatedAt());
        dto.setUpdatedAt(assessment.getUpdatedAt());
        dto.setQuestions(
            assessment.getQuestions().stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList())
        );
        return dto;
    }

    public Assessment toEntity(AssessmentRequest request) {
        return Assessment.builder()
                .examTitle(request.getExamTitle())
                .description(request.getDescription())
                .timeValue(request.getTimeValue())
                .timeUnit(request.getTimeUnit())
                .passMark(request.getPassMark())
                .status(request.getStatus() != null ? request.getStatus() : AssessmentStatus.INACTIVE)
                .build();
    }
}
