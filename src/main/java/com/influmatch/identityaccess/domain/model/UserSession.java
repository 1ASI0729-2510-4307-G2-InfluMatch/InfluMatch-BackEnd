package com.influmatch.identityaccess.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.Instant;


@Entity
@Getter @Setter @NoArgsConstructor
public class UserSession extends BaseEntity {

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
