package com.talentprobe.assessment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.talentprobe.assessment.enums.Language;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private UUID userId;
    private String name;
    private String email;
    private String phoneNumber;
    private Role role;
    private Status status;
    private Language language;
    private String idDocumentName;
    private String idDocumentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime activatedAt;
    private LocalDateTime deactivatedAt;
}