package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.AuthResponse;
import com.talentprobe.assessment.dto.LoginRequest;
import com.talentprobe.assessment.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {

        // Admin sends email + password → receives token
        return authService.login(request);
    }
}