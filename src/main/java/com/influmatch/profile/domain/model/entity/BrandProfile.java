package com.influmatch.profile.domain.model.entity;

import com.influmatch.profile.domain.model.valueobject.Attachment;
import com.influmatch.profile.domain.model.valueobject.Country;
import com.influmatch.profile.domain.model.valueobject.Link;
import com.influmatch.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandProfile extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sector;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "country", nullable = false))
    private Country country;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    private String location;

    @ElementCollection
    @CollectionTable(
        joinColumns = @JoinColumn(name = "brand_profile_id")
    )
    private List<Link> links = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        joinColumns = @JoinColumn(name = "brand_profile_id")
    )
    private List<Attachment> attachments = new ArrayList<>();

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    public BrandProfile(
            String name,
            String sector,
            Country country,
            String description,
            Long userId
    ) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (sector == null || sector.trim().isEmpty()) {
            throw new IllegalArgumentException("Sector cannot be empty");
        }
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        this.name = name.trim();
        this.sector = sector.trim();
        this.country = country;
        this.description = description.trim();
        this.userId = userId;
    }

    public void updateLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void updateProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public void updateWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl != null ? websiteUrl.trim() : null;
    }

    public void updateLocation(String location) {
        this.location = location != null ? location.trim() : null;
    }

    public void setLinks(List<Link> links) {
        this.links = links != null ? new ArrayList<>(links) : new ArrayList<>();
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
    }

    public void update(
            String name,
            String sector,
            Country country,
            String description,
            String websiteUrl,
            String location
    ) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
        if (sector != null && !sector.trim().isEmpty()) {
            this.sector = sector.trim();
        }
        if (country != null) {
            this.country = country;
        }
        if (description != null && !description.trim().isEmpty()) {
            this.description = description.trim();
        }
        
        updateWebsiteUrl(websiteUrl);
        updateLocation(location);
    }
} 