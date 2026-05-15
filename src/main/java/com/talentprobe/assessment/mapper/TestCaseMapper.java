package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.TestCaseDto;
import com.talentprobe.assessment.entity.TestCase;
import org.springframework.stereotype.Component;

@Component
public class TestCaseMapper {

    public TestCaseDto toDto(TestCase testCase) {
        TestCaseDto dto = new TestCaseDto();
        dto.setTestCaseId(testCase.getTestCaseId());
        dto.setInput(testCase.getInput());
        dto.setExpectedOutput(testCase.getExpectedOutput());
        return dto;
    }
}
