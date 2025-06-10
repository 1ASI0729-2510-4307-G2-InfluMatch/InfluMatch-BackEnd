package com.influmatch.auth.infrastructure.security;

import com.influmatch.auth.domain.model.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CurrentUser extends User {
    private final Long id;
    private final UserRole role;
    private final boolean profileCompleted;

    public CurrentUser(
            Long id,
            String email,
            String password,
            UserRole role,
            boolean profileCompleted,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(email, password, authorities);
        this.id = id;
        this.role = role;
        this.profileCompleted = profileCompleted;
    }
} 