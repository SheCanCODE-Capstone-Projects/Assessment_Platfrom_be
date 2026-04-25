package com.talentprobe.assessment.config;

import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile; // <-- ADD THIS IMPORT
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByRole(Role.ADMIN)) {
            User superAdmin = User.builder()
                    .name("System Admin")
                    .email("aria@gmail.com")
                    .password(passwordEncoder.encode("abcd"))
                    .role(Role.ADMIN)
                    .status(Status.ACTIVE)
                    .build();
            userRepository.save(superAdmin);
            System.out.println("Admin created: aria@gmail.com / abcd");
        }
    }
}