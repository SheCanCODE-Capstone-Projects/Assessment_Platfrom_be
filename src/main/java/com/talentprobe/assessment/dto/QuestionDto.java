package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.Difficulty;
import com.talentprobe.assessment.enums.Language;
import lombok.Data;

import java.util.UUID;

@Data
public class QuestionDto {
    private UUID questionId;
    private String title;
    private String description;
    private Integer marks;
    private Difficulty difficulty;
    private Language language;
    private String starterCode;
}
