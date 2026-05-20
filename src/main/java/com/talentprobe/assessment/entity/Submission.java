package com.talentprobe.assessment.entity;

import com.talentprobe.assessment.enums.MarkingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "submission_id")
    private UUID submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_assessment_id", nullable = false)
    private CandidateAssessment candidateAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "score_awarded", nullable = false)
    @Builder.Default
    private Integer scoreAwarded = 0;

    @Column(name = "total_test_cases")
    private Integer totalTestCases;

    @Column(name = "passed_test_cases")
    private Integer passedTestCases;

    @Column(name = "admin_review_note", columnDefinition = "TEXT")
    private String adminReviewNote;

    @Column(name = "admin_reviewed", nullable = false)
    @Builder.Default
    private Boolean adminReviewed = false;

    // PENDING when submitted, updated to PASSED or FAILED after scoring
    @Enumerated(EnumType.STRING)
    @Column(name = "marking_status", nullable = false)
    @Builder.Default
    private MarkingStatus markingStatus = MarkingStatus.PENDING;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
        if (this.markingStatus == null) {
            this.markingStatus = MarkingStatus.PENDING;
        }
    }
}
