package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.Difficulty;
import com.talentprobe.assessment.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Integer marks;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Language language;

    @NotBlank
    private String starterCode;
}
