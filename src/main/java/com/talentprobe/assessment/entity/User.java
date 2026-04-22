package com.talentprobe.assessment.entity;

import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private String idDocumentPath;

    private String profileImagePath;
}