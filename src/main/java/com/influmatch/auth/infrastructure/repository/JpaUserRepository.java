package com.influmatch.auth.infrastructure.repository;

import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.valueobject.Email;
import com.influmatch.auth.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
} 