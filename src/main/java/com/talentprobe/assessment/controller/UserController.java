package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.ApiResponse;
import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.dto.UserPatchRequest;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Language;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @Tag(name = "Public - Candidate Registration")
    @Operation(summary = "Register candidate", description = "All fields required.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserResponseDto>> createCandidate(
            @RequestParam("name") @NotBlank(message = "Name is required")
            @Parameter(description = "Full name", required = true) String name,

            @RequestParam("email") @Email(message = "Email must be valid") @NotBlank(message = "Email is required")
            @Parameter(description = "Email address", required = true) String email,

            @RequestParam("phoneNumber") @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits") @NotBlank(message = "Phone number is required")
            @Parameter(description = "Phone with country code", required = true) String phoneNumber,

            @RequestParam("language") @NotNull(message = "Programming language is required")
            @Parameter(description = "Choose programming language", required = true, schema = @Schema(implementation = Language.class)) Language language,

            @RequestPart(value = "idDocument", required = false)
            @Parameter(description = "Upload ID: PDF, JPG, PNG, max 10MB", required = true) MultipartFile idDocument
    ) throws IOException {
        if (idDocument == null || idDocument.isEmpty()) {
            throw new IllegalArgumentException("ID document is required");
        }

        UserDto dto = new UserDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPhoneNumber(phoneNumber);
        dto.setLanguage(language);
        dto.setIdDocument(idDocument);

        UserResponseDto created = userService.createCandidate(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Candidate registered successfully", created));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all candidates", description = "ADMIN only. Returns CANDIDATE.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllCandidates() {
        List<UserResponseDto> users = userService.getAllCandidates();
        return ResponseEntity.ok(ApiResponse.success("Successfully Retrieved", users));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("Successfully Retrieved", user));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Full update candidate", description = "ADMIN only. All fields required.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable UUID id,

            @RequestParam("name") @NotBlank(message = "Name is required")
            @Parameter(description = "Full name", required = true) String name,

            @RequestParam("email") @Email(message = "Email must be valid") @NotBlank(message = "Email is required")
            @Parameter(description = "Email address", required = true) String email,

            @RequestParam("phoneNumber") @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits") @NotBlank(message = "Phone number is required")
            @Parameter(description = "Phone with country code", required = true) String phoneNumber,

            @RequestParam("language") @NotNull(message = "Programming language is required")
            @Parameter(description = "Choose programming language", required = true, schema = @Schema(implementation = Language.class)) Language language
    ) {
        UserDto dto = new UserDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPhoneNumber(phoneNumber);
        dto.setLanguage(language);
        dto.setIdDocument(null);

        UserResponseDto updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Successfully Updated", updated));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Partial update candidate")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserResponseDto>> patchUser(
            @PathVariable UUID id,

            @RequestParam(value = "name", required = false)
            @Parameter(description = "New full name", required = false) String name,

            @RequestParam(value = "phoneNumber", required = false)
            @Parameter(description = "New phone with country code", required = false) String phoneNumber,

            @RequestParam(value = "language", required = false)
            @Parameter(description = "Choose programming language", required = false, schema = @Schema(implementation = Language.class)) Language language,

            @RequestPart(value = "idDocument", required = false)
            @Parameter(description = "Upload new ID: PDF, JPG, PNG, max 10MB", required = false) MultipartFile idDocument
    ) throws IOException {
        UserPatchRequest dto = new UserPatchRequest();
        dto.setName(name);
        dto.setPhoneNumber(phoneNumber);
        dto.setLanguage(language);
        dto.setIdDocument(idDocument);

        UserResponseDto updated = userService.patchUser(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Successfully Updated", updated));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateUser(@PathVariable UUID id) {
        UserResponseDto updated = userService.updateUserStatus(id, Status.ACTIVE);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", updated));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDto>> deactivateUser(@PathVariable UUID id) {
        UserResponseDto updated = userService.updateUserStatus(id, Status.INACTIVE);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", updated));
    }

    @Tag(name = "Admin - User Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Download candidate ID document", description = "ADMIN only. Returns file with original name and type.")
    @GetMapping("/{id}/id-document")
    public ResponseEntity<Resource> downloadIdDocument(@PathVariable UUID id) {
        User user = userService.getUserEntity(id);
        byte[] document = userService.downloadIdDocument(id);
        ByteArrayResource resource = new ByteArrayResource(document);

        String filename = user.getIdDocumentName() != null ? user.getIdDocumentName() : "id-document";
        String contentType = user.getIdDocumentType() != null ? user.getIdDocumentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
    @Tag(name = "Admin - Management")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/replace")
    @Operation(summary = "Replace admin credentials", description = "Use when admin is fired. Updates current admin's name/email/password.")
    public ResponseEntity<ApiResponse<UserResponseDto>> replaceAdmin(
            @RequestParam @NotBlank String name,
            @RequestParam @Email @NotBlank String email,
            @RequestParam @NotBlank String newPassword
    ) {
        UserResponseDto updated = userService.replaceAdmin(name, email, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Admin credentials replaced successfully", updated));
    }
}