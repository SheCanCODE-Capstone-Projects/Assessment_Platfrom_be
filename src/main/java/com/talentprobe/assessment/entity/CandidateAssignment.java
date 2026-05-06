package com.talentprobe.assessment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidate_assignments")
@Builder

public class CandidateAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "assignment_id")
    private UUID assignmentId;

    // FK → Assessment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    // FK → Userthe candidate
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @Column(name = "secure_token", nullable = false, unique = true)
    private String secureToken;

    @Column(name = "access_link")
    private String accessLink;

    @Column(name = "invitation_sent", nullable = false)
    private boolean invitationSent = false;

    @Column(name = "link_expiry", nullable = false)
    private LocalDateTime linkExpiry;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;
}

