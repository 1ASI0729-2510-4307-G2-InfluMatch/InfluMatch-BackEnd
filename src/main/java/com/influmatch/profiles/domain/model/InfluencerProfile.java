package com.influmatch.profiles.domain.model;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.shared.domain.model.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class InfluencerProfile extends TimestampedEntity {

    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;                  // FK → users.user_id

    @Column(nullable = false)
    private String displayName;

    private String bio;
    private String category;
    private String country;
    private Long   followersCount = 0L;

    @CreationTimestamp private Instant createdAt;
    @UpdateTimestamp  private Instant updatedAt;
}
