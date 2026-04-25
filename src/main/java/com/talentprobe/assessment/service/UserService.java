package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.exception.DuplicateResourceException;
import com.talentprobe.assessment.exception.ResourceNotFoundException;
import com.talentprobe.assessment.mapper.UserMapper;
import com.talentprobe.assessment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto createCandidate(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }
        User user = userMapper.toEntity(dto);
        user.setRole(Role.CANDIDATE);
        user.setStatus(Status.ACTIVE);
        String tempPassword = "Temp" + UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(tempPassword));
        System.out.println("TEMP PASSWORD FOR " + dto.getEmail() + ": " + tempPassword);
        return userMapper.toDto(userRepository.save(user));
    }

    public List<UserResponseDto> getAllCandidates() {
        return userRepository.findByRoleAndStatus(Role.CANDIDATE, Status.ACTIVE)
                .stream().map(userMapper::toDto).toList();
    }

    public UserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public UserResponseDto updateUser(UUID id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}