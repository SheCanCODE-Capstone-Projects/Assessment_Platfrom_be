package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.QuestionDto;
import com.talentprobe.assessment.dto.QuestionRequest;
import com.talentprobe.assessment.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public QuestionDto toDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionId(question.getQuestionId());
        dto.setTitle(question.getTitle());
        dto.setDescription(question.getDescription());
        dto.setMarks(question.getMarks());
        dto.setDifficulty(question.getDifficulty());
        dto.setLanguage(question.getLanguage());
        dto.setStarterCode(question.getStarterCode());
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
}
