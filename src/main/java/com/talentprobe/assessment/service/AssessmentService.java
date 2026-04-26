package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.AssessmentDto;
import com.talentprobe.assessment.dto.AssessmentRequest;
import com.talentprobe.assessment.entity.Assessment;
import com.talentprobe.assessment.mapper.AssessmentMapper;
import com.talentprobe.assessment.repository.AssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentMapper assessmentMapper;

    public AssessmentDto createAssessment(AssessmentRequest request) {
        return assessmentMapper.toDto(assessmentRepository.save(assessmentMapper.toEntity(request)));
    }

    public AssessmentDto getAssessmentById(UUID id) {
        return assessmentMapper.toDto(
                assessmentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id))
        );
    }

    public List<AssessmentDto> getAllAssessments() {
        return assessmentRepository.findAll()
                .stream()
                .map(assessmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public AssessmentDto updateAssessment(UUID id, AssessmentRequest request) {
        Assessment existing = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));
        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setTimeValue(request.getTimeValue());
        existing.setTimeUnit(request.getTimeUnit());
        existing.setPassMark(request.getPassMark());
        return assessmentMapper.toDto(assessmentRepository.save(existing));
    }

    public void deleteAssessment(UUID id) {
        if (!assessmentRepository.existsById(id)) {
            throw new RuntimeException("Assessment not found with id: " + id);
        }
        assessmentRepository.deleteById(id);
    }
}
