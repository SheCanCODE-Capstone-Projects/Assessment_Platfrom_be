package com.talentprobe.assessment.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TestCaseDto {
    private UUID testCaseId;
    private String input;
    private String expectedOutput;
}
