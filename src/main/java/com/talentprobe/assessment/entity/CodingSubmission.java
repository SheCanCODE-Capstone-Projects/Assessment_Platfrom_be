package com.talentprobe.assessment.entity;

import com.talentprobe.assessment.enums.GradingStatus;
import com.talentprobe.assessment.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "coding_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coding_submission_id")
    private UUID codingSubmissionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false, unique = true)
    private Submission submission;

    @Column(name = "source_code", nullable = false, columnDefinition = "TEXT")
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "grading_status", nullable = false)
    @Builder.Default
    private GradingStatus gradingStatus = GradingStatus.PENDING;
}
