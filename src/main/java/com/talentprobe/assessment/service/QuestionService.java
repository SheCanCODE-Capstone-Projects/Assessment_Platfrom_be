package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.QuestionDto;
import com.talentprobe.assessment.dto.QuestionRequest;
import com.talentprobe.assessment.entity.Question;
import com.talentprobe.assessment.mapper.QuestionMapper;
import com.talentprobe.assessment.repository.AssessmentRepository;
import com.talentprobe.assessment.repository.QuestionRepository;
import com.talentprobe.assessment.repository.TestCaseRepository;
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
    private final TestCaseRepository testCaseRepository;
    private final QuestionMapper questionMapper;

    public QuestionDto createQuestion(QuestionRequest request) {
        Question saved = questionRepository.save(questionMapper.toEntity(request));
        testCaseRepository.saveAll(questionMapper.toTestCaseEntities(request, saved));
        return questionMapper.toDto(saved);
    }

    public QuestionDto getQuestionById(UUID id) {
        return questionMapper.toDto(
                questionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + id))
        );
    }

    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDto> getQuestionsByAssessment(UUID assessmentId) {
        return assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + assessmentId))
                .getQuestions()
                .stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public QuestionDto updateQuestion(UUID id, QuestionRequest request) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setMarks(request.getMarks());
        existing.setDifficulty(request.getDifficulty());
        existing.setLanguage(request.getLanguage());
        existing.setStarterCode(request.getStarterCode());
        Question saved = questionRepository.save(existing);
        if (request.getTestCases() != null) {
            testCaseRepository.deleteAll(testCaseRepository.findByQuestion_QuestionId(id));
            testCaseRepository.saveAll(questionMapper.toTestCaseEntities(request, saved));
        }
        return questionMapper.toDto(saved);
    }

    public void deleteQuestion(UUID id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        testCaseRepository.deleteAll(testCaseRepository.findByQuestion_QuestionId(id));
        questionRepository.deleteById(id);
    }
}
