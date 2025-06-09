package com.influmatch.auth.domain.repository;

import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.valueobject.Email;
import java.util.Optional;
/**
 * Interfaz de repositorio de User en el dominio.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
}
