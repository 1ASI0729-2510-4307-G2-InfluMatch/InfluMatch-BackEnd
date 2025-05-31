package com.influmatch.ratings.domain.model;

import com.influmatch.collaboration.domain.model.Campaign;
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
public class Rating extends BaseEntity {
  
    @ManyToOne(fetch = FetchType.LAZY)         // quien escribe
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)         // a quién califica
    @JoinColumn(name = "target_id")
    private User target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;                 // nullable

    @Column(nullable = false)
    private short score;                       // 1-5

    private String comment;

    @CreationTimestamp
    private Instant createdAt;
}
