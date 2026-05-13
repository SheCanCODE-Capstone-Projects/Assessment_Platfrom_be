package com.talentprobe.assessment.dto;

import lombok.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseDTO {
    private UUID assignmentId;

    // From Assessment
    private UUID assessmentId;
    private String assessmentTitle;

    // From User (candidate)
    private UUID candidateId;
    private String candidateName;
    private String candidateEmail;

    // Assignment details
    private String secureToken;
    private String accessLink;
    private boolean invitationSent;
    private LocalDateTime linkExpiry;
    private LocalDateTime assignedAt;

}
