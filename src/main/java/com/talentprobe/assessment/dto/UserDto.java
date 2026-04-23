package com.talentprobe.assessment.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto { // 1. Role: KEEPING NAME UserDto like you requested
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone must be 10-15 digits")
    @NotBlank(message = "Phone is required for candidates")
    private String phoneNumber; // 2. Role: Required for candidate per lecturer

    // 3. NO PASSWORD: Candidate doesn't choose password. You generate temp one in Service
    // 4. NO ROLE: Service forces Role.CANDIDATE. Prevents user from making themselves admin
}