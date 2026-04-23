package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping // 1. Admin creates candidate
    public UserResponseDto createCandidate(@Valid @RequestBody UserDto dto) {
        return userService.createCandidate(dto);
    }

    @GetMapping // 2. List candidates
    public List<UserResponseDto> getAllCandidates() {
        return userService.getAllCandidates();
    }

    @GetMapping("/{id}") // 3. Get candidate
    public UserResponseDto getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}") // 4. Update candidate
    public UserResponseDto updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}") // 5. Delete candidate
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}