package com.influmatch.profiles.application;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.identityaccess.domain.repository.UserRepository;
import com.influmatch.media.application.service.MediaService;
import com.influmatch.media.domain.model.MediaFile;
import com.influmatch.media.api.dto.MediaUploadRequest;
import com.influmatch.media.domain.model.MediaType;
import com.influmatch.profiles.domain.model.InfluencerProfile;
import com.influmatch.profiles.domain.repository.InfluencerProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfluencerService {

    private final InfluencerProfileRepository influencerRepo;
    private final UserRepository userRepo;
    private final MediaService mediaService;

    @Transactional(readOnly = true)
    public List<InfluencerProfile> findAll() {
        return influencerRepo.findAll();
    }

    @Transactional(readOnly = true)
    public InfluencerProfile findById(Long id) {
        return influencerRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("influencer_not_found"));
    }

    @Transactional
    public InfluencerProfile create(Long userId, String displayName, String bio, 
                                  String category, String country, Long followersCount,
                                  String profilePictureBase64) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("user_not_found"));

        InfluencerProfile profile = new InfluencerProfile();
        profile.setUser(user);
        profile.setDisplayName(displayName);
        profile.setBio(bio);
        profile.setCategory(category);
        profile.setCountry(country);
        profile.setFollowersCount(followersCount != null ? followersCount : 0L);

        // Manejar la foto de perfil si se proporciona
        if (profilePictureBase64 != null && !profilePictureBase64.isEmpty()) {
            MediaUploadRequest mediaRequest = new MediaUploadRequest();
            mediaRequest.setBase64Content(profilePictureBase64);
            mediaRequest.setFilename("profile_picture.jpg");
            mediaRequest.setContentType("image/jpeg");
            mediaRequest.setType(MediaType.IMAGE);
            mediaRequest.setDescription("Foto de perfil de influencer");
            mediaRequest.setTitle("Foto de perfil");

            MediaFile profilePicture = mediaService.uploadFile(mediaRequest, userId);
            profile.setProfilePicture(profilePicture);
        }

        return influencerRepo.save(profile);
    }

    @Transactional
    public InfluencerProfile update(Long id, String displayName, String bio,
                                  String category, String country, Long followersCount,
                                  String profilePictureBase64) {
        InfluencerProfile profile = findById(id);

        profile.setDisplayName(displayName);
        profile.setBio(bio);
        profile.setCategory(category);
        profile.setCountry(country);
        profile.setFollowersCount(followersCount != null ? followersCount : 0L);

        // Manejar la foto de perfil si se proporciona
        if (profilePictureBase64 != null && !profilePictureBase64.isEmpty()) {
            MediaUploadRequest mediaRequest = new MediaUploadRequest();
            mediaRequest.setBase64Content(profilePictureBase64);
            mediaRequest.setFilename("profile_picture.jpg");
            mediaRequest.setContentType("image/jpeg");
            mediaRequest.setType(MediaType.IMAGE);
            mediaRequest.setDescription("Foto de perfil de influencer");
            mediaRequest.setTitle("Foto de perfil");

            MediaFile profilePicture = mediaService.uploadFile(mediaRequest, profile.getUser().getId());
            profile.setProfilePicture(profilePicture);
        }

        return influencerRepo.save(profile);
    }

    @Transactional
    public void delete(Long id) {
        if (!influencerRepo.existsById(id)) {
            throw new EntityNotFoundException("influencer_not_found");
        }
        influencerRepo.deleteById(id);
    }
} 