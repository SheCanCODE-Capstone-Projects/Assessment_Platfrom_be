package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.AssignmentResponseDTO;
import com.talentprobe.assessment.entity.CandidateAssignment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {
    @Value("${app.base-url}")
    private String baseUrl;
    // Convert Entity → ResponseDTO
    public AssignmentResponseDTO toResponseDTO(CandidateAssignment assignment) {
        return AssignmentResponseDTO.builder()
                .assignmentId(assignment.getAssignmentId())
                .assessmentId(assignment.getAssessment().getAssessmentId())
                .assessmentTitle(assignment.getAssessment().getExamTitle())
                .candidateId(assignment.getCandidate().getUserId())
                .candidateName(assignment.getCandidate().getName())
                .candidateEmail(assignment.getCandidate().getEmail())
                .accessLink(baseUrl + "/" + assignment.getSecureToken())
                .secureToken(assignment.getSecureToken())
                .invitationSent(assignment.isInvitationSent())
                .linkExpiry(assignment.getLinkExpiry())
                .assignedAt(assignment.getAssignedAt())
                .build();
    }
}
