package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Used during login
    Optional<User> findByEmail(String email);
}