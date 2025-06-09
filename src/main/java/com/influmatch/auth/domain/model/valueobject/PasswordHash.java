package com.influmatch.auth.domain.model.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Embeddable
@EqualsAndHashCode
@Getter
public final class PasswordHash {

    @Column(name = "password_hash", nullable = false)
    private String hash;

    // Constructor para JPA
    protected PasswordHash() {}

    public PasswordHash(@NonNull String hash) {
        if (hash.length() < 60) {
            throw new IllegalArgumentException("Hash de contraseña inválido");
        }
        this.hash = hash;
    }

    @Override
    public String toString() {
        return hash;
    }
}
