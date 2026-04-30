package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserPatchRequest {
    @Size(min = 1, message = "Name cannot be blank if provided")
    @Schema(description = "New full name", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "John Doe")
    private String name;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    @Schema(description = "New phone with country code", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "+250788123456")
    private String phoneNumber;

    @Schema(description = "Choose programming language", requiredMode = Schema.RequiredMode.NOT_REQUIRED, implementation = Language.class)
    private Language language;

    @Schema(type = "string", format = "binary", description = "Upload new ID: PDF, JPG, PNG only, max 10MB", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private MultipartFile idDocument;
}