package com.influmatch.profile.domain.model.valueobject;

import com.influmatch.profile.domain.exception.ProfileException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;

@Embeddable
@Getter
@NoArgsConstructor
public class Link {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    public Link(String title, String url) {
        setTitle(title);
        setUrl(url);
    }

    private void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new ProfileException("Link title cannot be empty");
        }
        this.title = title.trim();
    }

    private void setUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new ProfileException("Link URL cannot be empty");
        }
        try {
            new URI(url);
            this.url = url.trim();
        } catch (URISyntaxException e) {
            throw new ProfileException("Invalid URL format: " + url);
        }
    }
} 