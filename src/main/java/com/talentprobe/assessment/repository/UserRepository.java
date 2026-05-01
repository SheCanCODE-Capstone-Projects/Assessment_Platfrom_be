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

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);

    List<User> findByRoleAndStatusNotAndDeletedAtIsNull(Role role, Status status);
    Optional<User> findByUserIdAndStatusNotAndDeletedAtIsNull(UUID userId, Status status);

    long countByRoleAndStatusNot(Role role, Status status);

    List<User> findByRoleAndStatusAndDeletedAtIsNull(Role role, Status status);
    List<User> findByDeletedAtIsNull();
}