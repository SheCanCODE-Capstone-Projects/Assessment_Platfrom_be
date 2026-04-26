package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.QuestionDto;
import com.talentprobe.assessment.dto.QuestionRequest;
import com.talentprobe.assessment.entity.Assessment;
import com.talentprobe.assessment.entity.Question;
import com.talentprobe.assessment.mapper.QuestionMapper;
import com.talentprobe.assessment.repository.AssessmentRepository;
import com.talentprobe.assessment.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuestionMapper questionMapper;

    public QuestionDto createQuestion(QuestionRequest request) {
        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + request.getAssessmentId()));

        Question question = Question.builder()
                .promptText(request.getPromptText())
                .defaultMarks(request.getDefaultMarks())
                .assessment(assessment)
                .build();

        return questionMapper.toDto(questionRepository.save(question));
    }

    public QuestionDto getQuestionById(UUID id) {
        return questionMapper.toDto(
                questionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + id))
        );
    }

    public List<QuestionDto> getQuestionsByAssessment(UUID assessmentId) {
        return questionRepository.findByAssessment_AssessmentId(assessmentId)
                .stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public QuestionDto updateQuestion(UUID id, QuestionRequest request) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        existing.setPromptText(request.getPromptText());
        existing.setDefaultMarks(request.getDefaultMarks());
        if (!existing.getAssessment().getAssessmentId().equals(request.getAssessmentId())) {
            Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                    .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + request.getAssessmentId()));
            existing.setAssessment(assessment);
        }
        return questionMapper.toDto(questionRepository.save(existing));
    }

    public void deleteQuestion(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }
}
