package com.talentprobe.assessment.repository;

import com.talentprobe.assessment.entity.User;
import com.talentprobe.assessment.enums.Role;
import com.talentprobe.assessment.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    // DataLoader needs it to check if admin exists
    long countByRoleAndStatus(Role role, Status status);

    List<User> findByRoleAndStatus(Role role, Status status);
}