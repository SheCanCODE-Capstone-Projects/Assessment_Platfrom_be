package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.ApiResponse;
import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.dto.UserPatchRequest;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createCandidate(@Valid @RequestBody UserDto dto) {
        UserResponseDto created = userService.createCandidate(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Candidate created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllCandidates() {
        List<UserResponseDto> users = userService.getAllCandidates();
        return ResponseEntity.ok(
                ApiResponse.success("Candidates retrieved successfully", users)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(
                ApiResponse.success("successfully Retrieved ", user)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto dto) {
        UserResponseDto updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success("successfully Updated ", updated)
        );
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> patchUser(
            @PathVariable UUID id,
            @RequestBody UserPatchRequest dto) {

        UserResponseDto updated = userService.patchUser(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success("Profile updated successfully", updated)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDto>> deactivateUser(@PathVariable UUID id) {
        UserResponseDto updated = userService.updateUserStatus(id, Status.INACTIVE);
        return ResponseEntity.ok(
                ApiResponse.success("User deactivated successfully", updated)
        );
    }
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateUser(@PathVariable UUID id) {
        UserResponseDto updated = userService.updateUserStatus(id, Status.ACTIVE);
        return ResponseEntity.ok(
                ApiResponse.success("User activated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully")
        );
    }
}