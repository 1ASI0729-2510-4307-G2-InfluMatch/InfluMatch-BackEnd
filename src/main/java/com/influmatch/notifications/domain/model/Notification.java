package com.influmatch.notifications.domain.model;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Notification extends BaseEntity  {

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
