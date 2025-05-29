package com.influmatch.messaging.domain.model;

import com.influmatch.collaboration.domain.model.Campaign;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                          // → dialogs.id

    /** Dialogo puede estar asociado a una campaña (o ser independiente) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")         // FK nullable
    private Campaign campaign;

    @CreationTimestamp
    private Instant createdAt;
}