package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.QuestionDto;
import com.talentprobe.assessment.dto.QuestionRequest;
import com.talentprobe.assessment.entity.Question;
import com.talentprobe.assessment.entity.TestCase;
import com.talentprobe.assessment.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final TestCaseMapper testCaseMapper;
    private final TestCaseRepository testCaseRepository;

    public QuestionDto toDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionId(question.getQuestionId());
        dto.setTitle(question.getTitle());
        dto.setDescription(question.getDescription());
        dto.setMarks(question.getMarks());
        dto.setDifficulty(question.getDifficulty());
        dto.setLanguage(question.getLanguage());
        dto.setStarterCode(question.getStarterCode());
        dto.setTestCases(
            testCaseRepository.findByQuestion_QuestionId(question.getQuestionId())
                .stream()
                .map(testCaseMapper::toDto)
                .collect(Collectors.toList())
        );
        return dto;
    }

    public Question toEntity(QuestionRequest request) {
        return Question.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .marks(request.getMarks())
                .difficulty(request.getDifficulty())
                .language(request.getLanguage())
                .starterCode(request.getStarterCode())
                .build();
    }

    public List<TestCase> toTestCaseEntities(QuestionRequest request, Question question) {
        if (request.getTestCases() == null || request.getTestCases().isEmpty()) {
            return Collections.emptyList();
        }
        return request.getTestCases().stream()
                .map(tc -> TestCase.builder()
                        .input(tc.getInput())
                        .expectedOutput(tc.getExpectedOutput())
                        .question(question)
                        .build())
                .collect(Collectors.toList());
    }
}
