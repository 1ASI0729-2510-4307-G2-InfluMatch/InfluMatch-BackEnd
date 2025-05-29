package com.influmatch.identityaccess.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)      // Hibernate 6, map UUID ➜ database uuid
    private UUID id;                 // -> user_session**s**.id

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    private Instant createdAt;
}
