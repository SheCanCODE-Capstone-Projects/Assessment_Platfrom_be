package com.talentprobe.assessment.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone must be 10-15 digits")
    @NotBlank(message = "Phone is required for candidates")
    private String phoneNumber;


}