package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse { 
    private String accessToken;
    private Role role;
}