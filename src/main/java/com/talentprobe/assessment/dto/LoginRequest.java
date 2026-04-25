package com.talentprobe.assessment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    // Email used to identify admin
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    // Password entered by admin
    @NotBlank(message = "Password is required")
    private String password;
}