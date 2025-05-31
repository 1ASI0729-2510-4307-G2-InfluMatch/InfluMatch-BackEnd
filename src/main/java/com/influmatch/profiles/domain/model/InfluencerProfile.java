package com.influmatch.profiles.domain.model;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class InfluencerProfile extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;                  // FK → users.user_id

    @Column(nullable = false)
    private String displayName;

    private String bio;
    private String category;
    private String country;
    private Long   followersCount = 0L;

}
