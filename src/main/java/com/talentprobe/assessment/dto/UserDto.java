package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDto {
    @NotBlank(message = "Name is required")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "John Doe")
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "john@test.com")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    @NotBlank(message = "Phone number is required")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "+250788123456")
    private String phoneNumber;

    @NotNull(message = "Programming language is required")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Choose programming language")
    private Language language;

    @NotNull(message = "ID document is required")
    @Schema(type = "string", format = "binary", requiredMode = Schema.RequiredMode.REQUIRED, description = "PDF, JPG, PNG only, max 10MB")
    private MultipartFile idDocument;
}