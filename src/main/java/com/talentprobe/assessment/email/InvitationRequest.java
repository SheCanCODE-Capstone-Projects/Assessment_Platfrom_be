package com.talentprobe.assessment.email;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to send assessment invitation")
public class InvitationRequest {

    @Schema(example = "arianeabizera@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @Schema(example = "ariane", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(example = "http://localhost:3000/exam/abc123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Link is required")
    private String link;
}