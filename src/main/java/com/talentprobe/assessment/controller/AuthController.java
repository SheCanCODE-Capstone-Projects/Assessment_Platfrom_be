package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.ApiResponse;
import com.talentprobe.assessment.dto.LoginRequest;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.mapper.UserMapper;
import com.talentprobe.assessment.repository.UserRepository;
import com.talentprobe.assessment.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (user.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("User is deactivated");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getUserId().toString(),
                user.getRole().name()
        );

        Map<String, Object> data = Map.of(
                "token", token,
                "user", userMapper.toDto(user)
        );

        return ResponseEntity.ok(ApiResponse.success("Login successful", data));
    }
}