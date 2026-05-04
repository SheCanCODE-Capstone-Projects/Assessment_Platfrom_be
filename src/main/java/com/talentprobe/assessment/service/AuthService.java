package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.AuthResponse;
import com.talentprobe.assessment.dto.LoginRequest;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.exception.ResourceNotFoundException;
import com.talentprobe.assessment.mapper.UserMapper;
import com.talentprobe.assessment.repository.UserRepository;
import com.talentprobe.assessment.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getUserId().toString(),
                user.getRole().name()
        );

        return new AuthResponse(token, user.getRole());
    }

    public UserResponseDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponseDto(user);
    }
}