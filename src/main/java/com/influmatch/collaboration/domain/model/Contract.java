package com.influmatch.collaboration.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Contract extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", unique = true)
    private Campaign campaign;

    private String termsUrl;
    private Instant signedBrandAt;
    private Instant signedInflAt;

    @CreationTimestamp
    private Instant createdAt;
}
