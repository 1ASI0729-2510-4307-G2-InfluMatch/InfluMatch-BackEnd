package com.influmatch.identityaccess.domain.repository;

import com.influmatch.identityaccess.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
