package com.talentprobe.assessment.dto;

import lombok.Data;

@Data
public class AttemptStartRequest {
    // The secure token from the candidate's assignment link
    private String token;
}
