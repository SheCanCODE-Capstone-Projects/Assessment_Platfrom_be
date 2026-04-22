package com.talentprobe.assessment.service;

import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import com.talentprobe.assessment.mapper.UserMapper;
import com.talentprobe.assessment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public User createUser(UserDto dto) {

        User user = userMapper.toEntity(dto);

        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());

        user.setRole(Role.CANDIDATE);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}