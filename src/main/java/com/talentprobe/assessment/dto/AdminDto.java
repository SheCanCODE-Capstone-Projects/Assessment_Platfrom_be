package com.talentprobe.assessment.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminDto { // 1. Role: Separate DTO for Admin creation
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required for admin")
    @Size(min = 8, message = "Password must be 8+ characters")
    private String password; // 2. Role: Admin sets own password. Different from Candidate flow

    private String phoneNumber; // 3. Role: Optional for admin

    // 4. NO idDocumentPath: Admin never uploads ID
}