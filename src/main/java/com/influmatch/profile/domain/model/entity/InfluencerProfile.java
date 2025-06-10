package com.influmatch.profile.domain.model.entity;

import com.influmatch.profile.domain.model.valueobject.Attachment;
import com.influmatch.profile.domain.model.valueobject.Country;
import com.influmatch.profile.domain.model.valueobject.Link;
import com.influmatch.profile.domain.model.valueobject.SocialLink;
import com.influmatch.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InfluencerProfile extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(
        joinColumns = @JoinColumn(name = "influencer_profile_id")
    )
    @Column(name = "niche")
    private Set<String> niches = new HashSet<>();

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bio;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "country", nullable = false))
    private Country country;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Column(nullable = false)
    private Integer followers;

    @ElementCollection
    @CollectionTable(
        joinColumns = @JoinColumn(name = "influencer_profile_id")
    )
    private List<SocialLink> socialLinks = new ArrayList<>();

    private String location;

    @ElementCollection
    @CollectionTable(
        joinColumns = @JoinColumn(name = "influencer_profile_id")
    )
    private List<Link> links = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        joinColumns = @JoinColumn(name = "influencer_profile_id")
    )
    private List<Attachment> attachments = new ArrayList<>();

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    public InfluencerProfile(
            String name,
            Set<String> niches,
            String bio,
            Country country,
            Integer followers,
            Long userId
    ) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (niches == null || niches.isEmpty()) {
            throw new IllegalArgumentException("At least one niche is required");
        }
        if (bio == null || bio.trim().isEmpty()) {
            throw new IllegalArgumentException("Bio cannot be empty");
        }
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }
        if (followers == null || followers < 0) {
            throw new IllegalArgumentException("Followers must be a non-negative number");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        this.name = name.trim();
        this.niches = new HashSet<>(niches);
        this.bio = bio.trim();
        this.country = country;
        this.followers = followers;
        this.userId = userId;
    }

    public void updatePhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void updateProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public void updateLocation(String location) {
        this.location = location != null ? location.trim() : null;
    }

    public void setSocialLinks(List<SocialLink> socialLinks) {
        this.socialLinks = socialLinks != null ? new ArrayList<>(socialLinks) : new ArrayList<>();
    }

    public void setLinks(List<Link> links) {
        this.links = links != null ? new ArrayList<>(links) : new ArrayList<>();
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
    }

    public void update(
            String name,
            Set<String> niches,
            String bio,
            Country country,
            Integer followers,
            String location
    ) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
        if (niches != null && !niches.isEmpty()) {
            this.niches = new HashSet<>(niches);
        }
        if (bio != null && !bio.trim().isEmpty()) {
            this.bio = bio.trim();
        }
        if (country != null) {
            this.country = country;
        }
        if (followers != null && followers >= 0) {
            this.followers = followers;
        }
        
        updateLocation(location);
    }
} 