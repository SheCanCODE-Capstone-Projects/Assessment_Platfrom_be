package com.talentprobe.assessment.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class AssignmentRequestDTO {
    private UUID assessmentId;
    private UUID candidateId;
}
