package com.influmatch.messaging.domain.model;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.profiles.domain.model.MediaAsset;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                          // → messages.id

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    /** Texto del mensaje */
    private String body;

    /** Adjunta un asset opcional (imagen/video) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private MediaAsset asset;

    private Instant readAt;

    @CreationTimestamp
    private Instant createdAt;
}
