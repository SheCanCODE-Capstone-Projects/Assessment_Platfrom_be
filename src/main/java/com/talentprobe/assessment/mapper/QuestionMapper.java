package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.QuestionDto;
import com.talentprobe.assessment.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public QuestionDto toDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionId(question.getQuestionId());
        dto.setPromptText(question.getPromptText());
        dto.setDefaultMarks(question.getDefaultMarks());
        dto.setAssessmentId(question.getAssessment().getAssessmentId());
        return dto;
    }
}
