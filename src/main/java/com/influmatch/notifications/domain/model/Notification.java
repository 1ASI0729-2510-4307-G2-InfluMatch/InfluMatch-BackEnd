package com.influmatch.notifications.domain.model;

import com.influmatch.identityaccess.domain.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                           // → notifications.id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private NotifTypeEnum type;

    /** JSON payload; usa String o Map + converter */
    @Column(columnDefinition = "jsonb")
    private String payload;

    private Instant readAt;

    @CreationTimestamp
    private Instant createdAt;
}
