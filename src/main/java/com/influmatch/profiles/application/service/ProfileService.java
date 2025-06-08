package com.influmatch.profiles.application.service;

import com.influmatch.media.application.service.MediaService;
import com.influmatch.media.domain.model.MediaFile;
import com.influmatch.profiles.api.dto.CreateBrandProfileRequest;
import com.influmatch.profiles.api.dto.CreateInfluencerProfileRequest;
import com.influmatch.profiles.domain.model.BrandProfile;
import com.influmatch.profiles.domain.model.InfluencerProfile;
import com.influmatch.profiles.domain.repository.BrandProfileRepository;
import com.influmatch.profiles.domain.repository.InfluencerProfileRepository;
import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.identityaccess.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final BrandProfileRepository brandProfileRepository;
    private final InfluencerProfileRepository influencerProfileRepository;
    private final UserRepository userRepository;
    private final MediaService mediaService;

    @Transactional
    public BrandProfile createBrandProfile(CreateBrandProfileRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (brandProfileRepository.existsByUserId(userId)) {
            throw new RuntimeException("Brand profile already exists for this user");
        }

        BrandProfile profile = new BrandProfile();
        profile.setUser(user);
        profile.setCompanyName(request.getCompanyName());
        profile.setDescription(request.getDescription());
        profile.setIndustry(request.getIndustry());
        profile.setWebsiteUrl(request.getWebsiteUrl());
        profile.setLogoUrl(request.getLogoUrl());

        // Manejar la foto de perfil si se proporciona
        if (request.getProfilePicture() != null) {
            MediaFile profilePicture = mediaService.uploadFile(request.getProfilePicture(), userId);
            profile.setProfilePicture(profilePicture);
        }

        return brandProfileRepository.save(profile);
    }

    @Transactional
    public InfluencerProfile createInfluencerProfile(CreateInfluencerProfileRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (influencerProfileRepository.existsByUserId(userId)) {
            throw new RuntimeException("Influencer profile already exists for this user");
        }

        InfluencerProfile profile = new InfluencerProfile();
        profile.setUser(user);
        profile.setDisplayName(request.getDisplayName());
        profile.setBio(request.getBio());
        profile.setCategory(request.getCategory());
        profile.setCountry(request.getCountry());
        profile.setFollowersCount(request.getFollowersCount());

        // Manejar la foto de perfil si se proporciona
        if (request.getProfilePicture() != null) {
            MediaFile profilePicture = mediaService.uploadFile(request.getProfilePicture(), userId);
            profile.setProfilePicture(profilePicture);
        }

        return influencerProfileRepository.save(profile);
    }
} 