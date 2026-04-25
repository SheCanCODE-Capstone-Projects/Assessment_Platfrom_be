package com.talentprobe.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    // Temporary token (will be replaced by JWT later)
    private String token;
}