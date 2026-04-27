package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.AssessmentDto;
import com.talentprobe.assessment.dto.AssessmentRequest;
import com.talentprobe.assessment.entity.Assessment;
import com.talentprobe.assessment.entity.Question;
import com.talentprobe.assessment.enums.AssessmentStatus;
import com.talentprobe.assessment.mapper.AssessmentMapper;
import com.talentprobe.assessment.repository.AssessmentRepository;
import com.talentprobe.assessment.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final AssessmentMapper assessmentMapper;

    public AssessmentDto createAssessment(AssessmentRequest request) {
        Assessment assessment = assessmentMapper.toEntity(request);
        if (request.getQuestionIds() != null && !request.getQuestionIds().isEmpty()) {
            Set<Question> questions = new HashSet<>(questionRepository.findAllById(request.getQuestionIds()));
            assessment.setQuestions(questions);
        }
        return assessmentMapper.toDto(assessmentRepository.save(assessment));
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
        existing.setExamTitle(request.getExamTitle());
        existing.setDescription(request.getDescription());
        existing.setTimeValue(request.getTimeValue());
        existing.setTimeUnit(request.getTimeUnit());
        existing.setPassMark(request.getPassMark());
        if (request.getStatus() != null) existing.setStatus(request.getStatus());
        if (request.getQuestionIds() != null) {
            Set<Question> questions = new HashSet<>(questionRepository.findAllById(request.getQuestionIds()));
            existing.setQuestions(questions);
        }
        return assessmentMapper.toDto(assessmentRepository.save(existing));
    }

    public AssessmentDto assignQuestions(UUID id, List<UUID> questionIds) {
        Assessment existing = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));
        Set<Question> questions = new HashSet<>(questionRepository.findAllById(questionIds));
        existing.setQuestions(questions);
        return assessmentMapper.toDto(assessmentRepository.save(existing));
    }

    public AssessmentDto updateStatus(UUID id, AssessmentStatus status) {
        Assessment existing = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));
        existing.setStatus(status);
        return assessmentMapper.toDto(assessmentRepository.save(existing));
    }

    public void deleteAssessment(UUID id) {
        Assessment existing = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));
        if (!existing.getQuestions().isEmpty()) {
            throw new RuntimeException("Cannot delete assessment that still has questions. Please remove all questions first.");
        }
        assessmentRepository.deleteById(id);
    }
}
