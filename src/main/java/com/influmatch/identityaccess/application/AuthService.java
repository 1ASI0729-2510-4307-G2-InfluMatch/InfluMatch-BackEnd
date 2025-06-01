// src/main/java/com/influmatch/identityaccess/application/AuthService.java
package com.influmatch.identityaccess.application;

import com.influmatch.identityaccess.application.exceptions.EmailInUseException;
import com.influmatch.identityaccess.application.exceptions.InvalidCredentialsException;
import com.influmatch.identityaccess.domain.model.RoleEnum;
import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.identityaccess.domain.model.UserStatusEnum;
import com.influmatch.identityaccess.domain.repository.UserRepository;
import com.influmatch.identityaccess.domain.repository.UserSessionRepository;
import com.influmatch.security.JwtUtil;
import com.influmatch.security.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final UserSessionRepository sessionRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklist;

    /** 
     * Registra un nuevo usuario
     * @return Usuario registrado
     * @throws EmailInUseException si el email ya está registrado
     */
    @Transactional
    public User register(String email, String password, RoleEnum role) {
        // Verificar si el email existe
        if (userRepo.existsByEmail(email)) {
            log.info("Intento de registro con email existente: {}", email);
            throw new EmailInUseException();
        }

        // Crear usuario
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus(UserStatusEnum.ACTIVE);
        User savedUser = userRepo.save(user);
        
        log.info("Nuevo usuario registrado: {} con rol: {}", email, role);

        return savedUser;
    }

    /**
     * Genera un token JWT para el usuario
     */
    public String generateToken(User user) {
        return jwtUtil.generateToken(user);
    }

    /**
     * Autentica un usuario y retorna sus datos si las credenciales son válidas
     * @throws InvalidCredentialsException si las credenciales son inválidas
     */
    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("email_not_found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            log.info("Intento de login fallido para el email: {}", email);
            throw new InvalidCredentialsException("invalid_password");
        }

        if (user.getStatus() != UserStatusEnum.ACTIVE) {
            log.info("Intento de login con cuenta inactiva: {}", email);
            throw new InvalidCredentialsException();
        }

        log.info("Login exitoso para el usuario: {}", email);
        return user;
    }

    @Transactional
    public void logout(String token, Long userId) {
        // Invalida el token actual
        blacklist.blacklist(token, jwtUtil.getExpirationFromToken(token));
        
        // Elimina todas las sesiones del usuario
        sessionRepo.deleteAllByUserId(userId);
        
        log.info("User {} logged out successfully", userId);
    }
}
