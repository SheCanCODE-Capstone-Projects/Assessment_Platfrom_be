package com.talentprobe.assessment.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class BulkAssignmentRequestDTO {

    private UUID assessmentId;
    private List<UUID> candidateIds;
}
