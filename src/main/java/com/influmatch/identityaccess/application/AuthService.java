// src/main/java/com/influmatch/identityaccess/application/AuthService.java
package com.influmatch.identityaccess.application;

import com.influmatch.identityaccess.application.exceptions.EmailInUseException;
import com.influmatch.identityaccess.application.exceptions.InvalidCredentialsException;
import com.influmatch.identityaccess.domain.model.RoleEnum;
import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.identityaccess.domain.model.UserStatusEnum;
import com.influmatch.identityaccess.domain.repository.UserRepository;
import com.influmatch.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    /** 
     * Registra un nuevo usuario
     * @return Usuario registrado
     * @throws EmailInUseException si el email ya está registrado
     */
    @Transactional
    public User register(String email, String rawPassword, RoleEnum role) {
        // Verificar si el email existe
        if (users.existsByEmail(email)) {
            log.info("Intento de registro con email existente: {}", email);
            throw new EmailInUseException();
        }

        // Crear usuario
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(rawPassword));
        user.setRole(role);
        user.setStatus(UserStatusEnum.ACTIVE);
        users.save(user);
        
        log.info("Nuevo usuario registrado: {} con rol: {}", email, role);

        return user;
    }

    /**
     * Genera un token JWT para el usuario
     */
    public String generateToken(User user) {
        return jwt.generate(user.getId(), user.getRole().name());
    }

    /**
     * Autentica un usuario y retorna sus datos si las credenciales son válidas
     * @throws InvalidCredentialsException si las credenciales son inválidas
     */
    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = users.findByEmail(email)
            .orElseThrow(InvalidCredentialsException::new);

        if (!encoder.matches(password, user.getPasswordHash())) {
            log.info("Intento de login fallido para el email: {}", email);
            throw new InvalidCredentialsException();
        }

        if (user.getStatus() != UserStatusEnum.ACTIVE) {
            log.info("Intento de login con cuenta inactiva: {}", email);
            throw new InvalidCredentialsException();
        }

        log.info("Login exitoso para el usuario: {}", email);
        return user;
    }
}
