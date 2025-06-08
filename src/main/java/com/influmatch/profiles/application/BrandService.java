package com.influmatch.profiles.application;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.identityaccess.domain.repository.UserRepository;
import com.influmatch.media.application.service.MediaService;
import com.influmatch.media.domain.model.MediaFile;
import com.influmatch.media.api.dto.MediaUploadRequest;
import com.influmatch.media.domain.model.MediaType;
import com.influmatch.profiles.domain.model.BrandProfile;
import com.influmatch.profiles.domain.repository.BrandProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandProfileRepository brandRepo;
    private final UserRepository userRepo;
    private final MediaService mediaService;

    @Transactional(readOnly = true)
    public List<BrandProfile> findAll() {
        return brandRepo.findAll();
    }

    @Transactional(readOnly = true)
    public BrandProfile findById(Long id) {
        return brandRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("brand_not_found"));
    }

    @Transactional
    public BrandProfile create(Long userId, String companyName, String description, 
                             String industry, String websiteUrl, String logoUrl,
                             String profilePictureBase64) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("user_not_found"));

        BrandProfile profile = new BrandProfile();
        profile.setUser(user);
        profile.setCompanyName(companyName);
        profile.setDescription(description);
        profile.setIndustry(industry);
        profile.setWebsiteUrl(websiteUrl);
        profile.setLogoUrl(logoUrl);

        // Manejar la foto de perfil si se proporciona
        if (profilePictureBase64 != null && !profilePictureBase64.isEmpty()) {
            MediaUploadRequest mediaRequest = new MediaUploadRequest();
            mediaRequest.setBase64Content(profilePictureBase64);
            mediaRequest.setFilename("profile_picture.jpg");
            mediaRequest.setContentType("image/jpeg");
            mediaRequest.setType(MediaType.IMAGE);
            mediaRequest.setDescription("Foto de perfil de marca");
            mediaRequest.setTitle("Foto de perfil");

            MediaFile profilePicture = mediaService.uploadFile(mediaRequest, userId);
            profile.setProfilePicture(profilePicture);
        }

        return brandRepo.save(profile);
    }

    @Transactional
    public BrandProfile update(Long id, String companyName, String description,
                             String industry, String websiteUrl, String logoUrl,
                             String profilePictureBase64) {
        BrandProfile profile = findById(id);

        profile.setCompanyName(companyName);
        profile.setDescription(description);
        profile.setIndustry(industry);
        profile.setWebsiteUrl(websiteUrl);
        profile.setLogoUrl(logoUrl);

        // Manejar la foto de perfil si se proporciona
        if (profilePictureBase64 != null && !profilePictureBase64.isEmpty()) {
            MediaUploadRequest mediaRequest = new MediaUploadRequest();
            mediaRequest.setBase64Content(profilePictureBase64);
            mediaRequest.setFilename("profile_picture.jpg");
            mediaRequest.setContentType("image/jpeg");
            mediaRequest.setType(MediaType.IMAGE);
            mediaRequest.setDescription("Foto de perfil de marca");
            mediaRequest.setTitle("Foto de perfil");

            MediaFile profilePicture = mediaService.uploadFile(mediaRequest, profile.getUser().getId());
            profile.setProfilePicture(profilePicture);
        }

        return brandRepo.save(profile);
    }

    @Transactional
    public void delete(Long id) {
        if (!brandRepo.existsById(id)) {
            throw new EntityNotFoundException("brand_not_found");
        }
        brandRepo.deleteById(id);
    }
} 