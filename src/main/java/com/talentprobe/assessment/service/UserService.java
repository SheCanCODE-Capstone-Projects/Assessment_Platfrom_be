package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.BulkUploadResult;
import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.dto.UserPatchRequest;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Language;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.exception.DuplicateResourceException;
import com.talentprobe.assessment.exception.ResourceNotFoundException;
import com.talentprobe.assessment.mapper.UserMapper;
import com.talentprobe.assessment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "application/pdf"
    );

    @Transactional
    public UserResponseDto createCandidate(UserDto dto) throws IOException {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("This email is already registered");
        }

        User user = new User();
        user.setName(dto.getName().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPhoneNumber(dto.getPhoneNumber().trim());
        user.setLanguage(dto.getLanguage());
        user.setRole(Role.CANDIDATE);
        user.setStatus(Status.ACTIVE);
        user.setActivatedAt(LocalDateTime.now());

        if (dto.getIdDocument() != null && !dto.getIdDocument().isEmpty()) {
            validateFile(dto.getIdDocument());
            setDocument(user, dto.getIdDocument());
        }

        User saved = userRepository.save(user);
        return userMapper.toResponseDto(saved);
    }

    public List<UserResponseDto> getAllCandidates() {
        return userRepository.findByRoleAndStatusNotAndDeletedAtIsNull(Role.CANDIDATE, Status.DELETED).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(UUID id) {
        User user = getUserEntity(id);
        return userMapper.toResponseDto(user);
    }

    public User getUserEntity(UUID id) {
        return userRepository.findByUserIdAndStatusNotAndDeletedAtIsNull(id, Status.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserResponseDto updateUser(UUID id, UserDto dto) {
        User user = getUserEntity(id);

        String newEmail = dto.getEmail().trim().toLowerCase();
        if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new DuplicateResourceException("This email is already registered");
        }

        user.setName(dto.getName().trim());
        user.setEmail(newEmail);
        user.setPhoneNumber(dto.getPhoneNumber().trim());
        user.setLanguage(dto.getLanguage());

        User updated = userRepository.save(user);
        return userMapper.toResponseDto(updated);
    }

    @Transactional
    public UserResponseDto patchUser(UUID id, UserPatchRequest dto) throws IOException {
        User user = getUserEntity(id);

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName().trim());
        }

        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            String phone = dto.getPhoneNumber().trim();
            if (!phone.matches("^\\+?[0-9]{10,15}$")) {
                throw new IllegalArgumentException("Phone number must be 10-15 digits");
            }
            user.setPhoneNumber(phone);
        }

        if (dto.getLanguage() != null) {
            user.setLanguage(dto.getLanguage());
        }

        if (dto.getIdDocument() != null && !dto.getIdDocument().isEmpty()) {
            validateFile(dto.getIdDocument());
            setDocument(user, dto.getIdDocument());
        }

        User updated = userRepository.save(user);
        return userMapper.toResponseDto(updated);
    }

    @Transactional
    public UserResponseDto updateUserStatus(UUID id, Status status) {
        User user = getUserEntity(id);
        user.setStatus(status);
        if (status == Status.ACTIVE) {
            user.markAsActivated();
        } else if (status == Status.INACTIVE) {
            user.markAsDeactivated();
        }
        User updated = userRepository.save(user);
        return userMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = getUserEntity(id);

        if (user.getRole() == Role.ADMIN) {
            long activeAdminCount = userRepository.countByRoleAndStatusNot(Role.ADMIN, Status.DELETED);
            if (activeAdminCount <= 1) {
                throw new IllegalArgumentException("Cannot delete the last ACTIVE admin. Create another admin first.");
            }
        }

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getEmail().equals(currentEmail)) {
            throw new IllegalArgumentException("Cannot delete your own account. Ask another admin to delete it.");
        }

        user.setStatus(Status.DELETED);
        user.markAsDeleted();
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDto replaceAdmin(String name, String email, String newPassword) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only admin can replace themselves");
        }

        if (!admin.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already used by another user");
        }

        admin.setName(name.trim());
        admin.setEmail(email.trim().toLowerCase());
        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setStatus(Status.ACTIVE);
        admin.setActivatedAt(LocalDateTime.now());

        User updated = userRepository.save(admin);
        return userMapper.toResponseDto(updated);
    }

    public byte[] downloadIdDocument(UUID id) {
        User user = getUserEntity(id);
        if (user.getIdDocument() == null || user.getIdDocument().length == 0) {
            throw new ResourceNotFoundException("ID document not found for user: " + id);
        }
        return user.getIdDocument();
    }

    public UserResponseDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public BulkUploadResult bulkUploadCandidates(MultipartFile file) throws IOException {
        List<User> toSave = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int skipped = 0;

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String email = getCellString(row.getCell(0)).trim().toLowerCase();
                String name = getCellString(row.getCell(1)).trim();
                String phone = getCellString(row.getCell(2)).trim();
                String lang = getCellString(row.getCell(3)).trim();

                if (email.isBlank()) {
                    errors.add("Row " + (i + 1) + ": email is empty");
                    skipped++;
                    continue;
                }

                if (userRepository.existsByEmail(email)) {
                    errors.add("Row " + (i + 1) + ": " + email + " already exists");
                    skipped++;
                    continue;
                }

                try {
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setPhoneNumber(phone);
                    user.setLanguage(Language.valueOf(lang.toUpperCase()));
                    user.setRole(Role.CANDIDATE);
                    user.setStatus(Status.ACTIVE);
                    user.setActivatedAt(LocalDateTime.now());
                    toSave.add(user);
                } catch (IllegalArgumentException e) {
                    errors.add("Row " + (i + 1) + ": Invalid language '" + lang + "'");
                    skipped++;
                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    skipped++;
                }
            }
        }

        if (!toSave.isEmpty()) {
            userRepository.saveAll(toSave);
        }
        return new BulkUploadResult(toSave.size(), skipped, errors);
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("ID document is required");
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must not exceed 10MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("ID document must be PDF, JPG, or PNG");
        }
    }

    private void setDocument(User user, MultipartFile file) throws IOException {
        user.setIdDocument(file.getBytes());
        user.setIdDocumentName(file.getOriginalFilename());
        user.setIdDocumentType(file.getContentType());
    }
}