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
public class SocialLink {
    @Column(nullable = false)
    private String platform;

    @Column(nullable = false)
    private String url;

    public SocialLink(String platform, String url) {
        setPlatform(platform);
        setUrl(url);
    }

    private void setPlatform(String platform) {
        if (platform == null || platform.trim().isEmpty()) {
            throw new ProfileException("Social media platform cannot be empty");
        }
        this.platform = platform.trim();
    }

    private void setUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new ProfileException("Social media URL cannot be empty");
        }
        try {
            new URI(url);
            this.url = url.trim();
        } catch (URISyntaxException e) {
            throw new ProfileException("Invalid URL format: " + url);
        }
    }
} 