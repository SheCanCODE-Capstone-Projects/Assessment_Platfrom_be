package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserDto dto) {

        return userService.createUser(dto);
    }

    @GetMapping
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}