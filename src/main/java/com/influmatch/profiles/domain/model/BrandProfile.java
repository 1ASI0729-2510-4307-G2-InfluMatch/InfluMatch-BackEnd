package com.influmatch.profiles.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import com.influmatch.identityaccess.domain.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class BrandProfile extends BaseEntity {

   
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String companyName;

    private String description;
    private String industry;
    private String websiteUrl;
    private String logoUrl;

}
