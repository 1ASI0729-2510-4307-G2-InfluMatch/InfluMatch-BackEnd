package com.influmatch.auth.application.assembler;

import com.influmatch.auth.application.dto.RegisterRequest;
import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.valueobject.Email;
import com.influmatch.auth.domain.model.valueobject.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAssembler {
    private final PasswordEncoder passwordEncoder;

    public User toEntity(RegisterRequest request) {
        return new User(
            new Email(request.getEmail()),
            new Password(passwordEncoder.encode(request.getPassword())),
            request.getRole()
        );
    }
} 