package com.influmatch.collaboration.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                   // → contracts.id

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", unique = true)
    private Campaign campaign;

    private String termsUrl;
    private Instant signedBrandAt;
    private Instant signedInflAt;

    @CreationTimestamp
    private Instant createdAt;
}
