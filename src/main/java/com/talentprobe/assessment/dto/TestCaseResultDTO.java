package com.talentprobe.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResultDTO {

    private UUID testCaseId;
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private boolean passed;
}
