package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID userId;
    private String name;
    private String email;
    private String phoneNumber;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;
    private String idDocumentPath;
}