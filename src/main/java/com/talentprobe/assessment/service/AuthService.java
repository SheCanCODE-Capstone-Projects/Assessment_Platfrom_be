package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.AuthResponse;
import com.talentprobe.assessment.dto.LoginRequest;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {

        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        // 2. Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 3. Ensure ONLY ADMIN can login
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied: Only admin can login");
        }

        // 4. TEMP TOKEN
        String token = "TEMP-TOKEN-" + user.getEmail();

        return new AuthResponse(token);
    }
}