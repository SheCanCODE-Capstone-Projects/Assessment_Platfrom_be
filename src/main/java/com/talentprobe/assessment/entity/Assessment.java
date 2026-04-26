package com.talentprobe.assessment.entity;

import com.talentprobe.assessment.enums.TimeUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assessments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assessmentId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Integer timeValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeUnit timeUnit;

    @Column(nullable = false)
    private Double passMark;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
