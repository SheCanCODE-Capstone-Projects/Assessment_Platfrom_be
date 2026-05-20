package com.talentprobe.assessment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestCaseRequest {

    @NotBlank
    private String input;

    @NotBlank
    private String expectedOutput;
}
