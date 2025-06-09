package com.influmatch.auth.infrastructure.persistence;

import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.valueobject.Email;
import com.influmatch.auth.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    @Override
    default Optional<User> findByEmail(Email email) {
        return findByEmailValue(email.getValue());
    }

    @Override
    default boolean existsByEmail(Email email) {
        return existsByEmailValue(email.getValue());
    }

    Optional<User> findByEmailValue(String email);
    boolean existsByEmailValue(String email);
} 