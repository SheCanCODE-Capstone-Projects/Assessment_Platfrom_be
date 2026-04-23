package com.talentprobe.assessment.config;

import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. BEST PRACTICE: Create first admin on startup if none exists
        if (!userRepository.existsByRole(Role.ADMIN)) {
            User superAdmin = User.builder()
                    .name("System Admin")
                    .email("aria@gmail.com")
                    .password(passwordEncoder.encode("abcd")) // 2. CHANGE THIS AFTER DEPLOY
                    .role(Role.ADMIN)
                    .status(Status.ACTIVE)
                    .build();
            userRepository.save(superAdmin);
            System.out.println("Admin created: ari@gmail.com / abcd");
        }
    }
}