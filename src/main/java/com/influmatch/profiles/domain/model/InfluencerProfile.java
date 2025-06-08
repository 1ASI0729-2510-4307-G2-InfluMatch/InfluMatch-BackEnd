package com.influmatch.profiles.domain.model;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.shared.domain.model.BaseEntity;
import com.influmatch.profiles.domain.model.valueobjects.*;
import com.influmatch.media.domain.model.MediaFile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class InfluencerProfile extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_picture_id")
    private MediaFile profilePicture;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "display_name"))
    private DisplayName displayName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "bio"))
    private Bio bio;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "category"))
    private Category category;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "country"))
    private Country country;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "followers_count"))
    private FollowersCount followersCount;

    public String getDisplayName() {
        return displayName != null ? displayName.toString() : null;
    }

    public String getBio() {
        return bio != null ? bio.toString() : null;
    }

    public String getCategory() {
        return category != null ? category.toString() : null;
    }

    public String getCountry() {
        return country != null ? country.toString() : null;
    }

    public Long getFollowersCount() {
        return followersCount != null ? followersCount.getValue() : 0L;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName != null ? DisplayName.of(displayName) : null;
    }

    public void setBio(String bio) {
        this.bio = bio != null ? Bio.of(bio) : null;
    }

    public void setCategory(String category) {
        this.category = category != null ? Category.of(category) : null;
    }

    public void setCountry(String country) {
        this.country = country != null ? Country.of(country) : null;
    }

    public void setFollowersCount(Long followersCount) {
        this.followersCount = followersCount != null ? FollowersCount.of(followersCount) : FollowersCount.of(0L);
    }
}
