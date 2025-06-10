package com.influmatch.profile.domain.model.valueobject;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {
    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    @Column(nullable = false)
    private String mediaUrl;

    public Attachment(String title, String description, MediaType mediaType, String mediaUrl) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Attachment title cannot be empty");
        }
        if (mediaType == null) {
            throw new IllegalArgumentException("Media type cannot be null");
        }
        if (mediaUrl == null || mediaUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Media URL cannot be empty");
        }

        this.title = title.trim();
        this.description = description != null ? description.trim() : null;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl.trim();
    }
} 