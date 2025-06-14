package com.influmatch.profile.application.service;

import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.UserRole;
import com.influmatch.auth.domain.repository.UserRepository;
import com.influmatch.auth.infrastructure.security.CurrentUser;
import com.influmatch.profile.application.dto.*;
import com.influmatch.profile.domain.model.entity.BrandProfile;
import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.domain.model.valueobject.Attachment;
import com.influmatch.profile.domain.model.valueobject.Country;
import com.influmatch.profile.domain.model.valueobject.Link;
import com.influmatch.profile.domain.model.valueobject.MediaType;
import com.influmatch.profile.domain.model.valueobject.SocialLink;
import com.influmatch.profile.domain.repository.BrandProfileRepository;
import com.influmatch.profile.domain.repository.InfluencerProfileRepository;
import com.influmatch.profile.domain.exception.ProfileException;
import com.influmatch.profile.domain.exception.ProfileNotFoundException;
import com.influmatch.profile.domain.exception.ProfileAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final BrandProfileRepository brandProfileRepository;
    private final InfluencerProfileRepository influencerProfileRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public BrandProfileResponse createBrandProfile(CreateBrandProfileRequest request, MultipartFile logo, MultipartFile profilePhoto) {
        // Validar rol y existencia de perfil antes de procesar archivos
        User user = getCurrentUser();
        if (user.getRole() != UserRole.BRAND) {
            throw new ProfileException("User role mismatch: expected BRAND but was " + user.getRole());
        }
        if (brandProfileRepository.existsByUserId(user.getId()) || influencerProfileRepository.existsByUserId(user.getId())) {
            throw new ProfileAlreadyExistsException("Profile already exists for user ID: " + user.getId());
        }

        BrandProfile profile = new BrandProfile(
            request.getName(),
            request.getSector(),
            new Country(request.getCountry()),
            request.getDescription(),
            user.getId()
        );

        if (logo != null) {
            profile.updateLogoUrl(fileStorageService.storeFile(logo));
        } else if (request.getLogo() != null) {
            profile.updateLogoUrl(fileStorageService.storeBase64File(request.getLogo(), "png"));
        }

        if (profilePhoto != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeFile(profilePhoto));
        } else if (request.getProfilePhoto() != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeBase64File(request.getProfilePhoto(), "png"));
        }

        profile.updateWebsiteUrl(request.getWebsiteUrl());
        profile.updateLocation(request.getLocation());
        profile.setLinks(request.getLinks().stream()
                .map(dto -> new Link(dto.getTitle(), dto.getUrl()))
                .collect(Collectors.toList()));

        List<Attachment> attachments = request.getAttachments().stream()
                .map(dto -> new Attachment(
                    dto.getTitle(),
                    dto.getDescription(),
                    dto.getMediaType(),
                    fileStorageService.storeBase64File(dto.getData(), getExtensionForMediaType(dto.getMediaType()))
                ))
                .collect(Collectors.toList());
        profile.setAttachments(attachments);

        BrandProfile savedProfile = brandProfileRepository.save(profile);
        user.markProfileAsCompleted();
        userRepository.save(user);

        return toBrandProfileResponse(savedProfile);
    }

    @Transactional
    public InfluencerProfileResponse createInfluencerProfile(CreateInfluencerProfileRequest request, MultipartFile photo, MultipartFile profilePhoto, List<MultipartFile> attachmentFiles) {
        User user = getCurrentUser();
        validateUserRole(user, UserRole.INFLUENCER);
        validateProfileDoesNotExist(user.getId());

        InfluencerProfile profile = new InfluencerProfile(
            request.getName(),
            request.getNiches(),
            request.getBio(),
            new Country(request.getCountry()),
            request.getFollowers(),
            user.getId()
        );

        if (photo != null) {
            profile.updatePhotoUrl(fileStorageService.storeFile(photo));
        } else if (request.getPhoto() != null) {
            profile.updatePhotoUrl(fileStorageService.storeBase64File(request.getPhoto(), "png"));
        }

        if (profilePhoto != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeFile(profilePhoto));
        } else if (request.getProfilePhoto() != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeBase64File(request.getProfilePhoto(), "png"));
        }

        profile.updateLocation(request.getLocation());
        profile.setSocialLinks(request.getSocialLinks().stream()
                .map(dto -> new SocialLink(dto.getPlatform(), dto.getUrl()))
                .collect(Collectors.toList()));
        profile.setLinks(request.getLinks().stream()
                .map(dto -> new Link(dto.getTitle(), dto.getUrl()))
                .collect(Collectors.toList()));

        List<Attachment> attachments = new ArrayList<>();
        
        // Process attachment files if present
        if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
            for (int i = 0; i < attachmentFiles.size(); i++) {
                MultipartFile file = attachmentFiles.get(i);
                if (file != null && !file.isEmpty()) {
                    String extension = getFileExtension(file.getOriginalFilename());
                    MediaType mediaType = getMediaTypeFromExtension(extension);
                    
                    // If there's metadata in the request, use it
                    String title = "Attachment " + (i + 1);
                    String description = "Uploaded file " + file.getOriginalFilename();
                    if (request.getAttachments() != null && i < request.getAttachments().size()) {
                        AttachmentDto metadata = request.getAttachments().get(i);
                        title = metadata.getTitle();
                        description = metadata.getDescription();
                        mediaType = metadata.getMediaType();
                    }
                    
                    attachments.add(new Attachment(
                        title,
                        description,
                        mediaType,
                        fileStorageService.storeFile(file)
                    ));
                }
            }
        }
        // Only process base64 attachments if no files were provided
        else if (request.getAttachments() != null) {
            attachments.addAll(request.getAttachments().stream()
                .filter(dto -> dto.getData() != null && !dto.getData().isEmpty())
                .map(dto -> new Attachment(
                    dto.getTitle(),
                    dto.getDescription(),
                    dto.getMediaType(),
                    fileStorageService.storeBase64File(dto.getData(), getExtensionForMediaType(dto.getMediaType()))
                ))
                .collect(Collectors.toList()));
        }
        
        profile.setAttachments(attachments);

        InfluencerProfile savedProfile = influencerProfileRepository.save(profile);
        user.markProfileAsCompleted();
        userRepository.save(user);

        return toInfluencerProfileResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public BrandProfileResponse getBrandProfile() {
        User user = getCurrentUser();
        validateUserRole(user, UserRole.BRAND);

        BrandProfile profile = brandProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Brand profile not found for user ID: " + user.getId()));

        return toBrandProfileResponse(profile);
    }

    @Transactional(readOnly = true)
    public InfluencerProfileResponse getInfluencerProfile() {
        User user = getCurrentUser();
        validateUserRole(user, UserRole.INFLUENCER);

        InfluencerProfile profile = influencerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Influencer profile not found for user ID: " + user.getId()));

        return toInfluencerProfileResponse(profile);
    }

    @Transactional
    public BrandProfileResponse updateBrandProfile(CreateBrandProfileRequest request, MultipartFile logo, MultipartFile profilePhoto) {
        User user = getCurrentUser();
        validateUserRole(user, UserRole.BRAND);

        BrandProfile profile = brandProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Brand profile not found"));

        profile.update(
            request.getName(),
            request.getSector(),
            request.getCountry() != null ? new Country(request.getCountry()) : null,
            request.getDescription(),
            request.getWebsiteUrl(),
            request.getLocation()
        );

        if (logo != null) {
            profile.updateLogoUrl(fileStorageService.storeFile(logo));
        } else if (request.getLogo() != null) {
            profile.updateLogoUrl(fileStorageService.storeBase64File(request.getLogo(), "png"));
        }

        if (profilePhoto != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeFile(profilePhoto));
        } else if (request.getProfilePhoto() != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeBase64File(request.getProfilePhoto(), "png"));
        }

        if (request.getLinks() != null) {
            profile.setLinks(request.getLinks().stream()
                    .map(dto -> new Link(dto.getTitle(), dto.getUrl()))
                    .collect(Collectors.toList()));
        }

        if (request.getAttachments() != null) {
            List<Attachment> attachments = request.getAttachments().stream()
                    .map(dto -> new Attachment(
                        dto.getTitle(),
                        dto.getDescription(),
                        dto.getMediaType(),
                        fileStorageService.storeBase64File(dto.getData(), getExtensionForMediaType(dto.getMediaType()))
                    ))
                    .collect(Collectors.toList());
            profile.setAttachments(attachments);
        }

        return toBrandProfileResponse(brandProfileRepository.save(profile));
    }

    @Transactional
    public InfluencerProfileResponse updateInfluencerProfile(CreateInfluencerProfileRequest request, MultipartFile photo, MultipartFile profilePhoto, List<MultipartFile> attachmentFiles) {
        User user = getCurrentUser();
        validateUserRole(user, UserRole.INFLUENCER);

        InfluencerProfile profile = influencerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Influencer profile not found"));

        profile.update(
            request.getName(),
            request.getNiches(),
            request.getBio(),
            request.getCountry() != null ? new Country(request.getCountry()) : null,
            request.getFollowers(),
            request.getLocation()
        );

        if (photo != null) {
            profile.updatePhotoUrl(fileStorageService.storeFile(photo));
        } else if (request.getPhoto() != null) {
            profile.updatePhotoUrl(fileStorageService.storeBase64File(request.getPhoto(), "png"));
        }

        if (profilePhoto != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeFile(profilePhoto));
        } else if (request.getProfilePhoto() != null) {
            profile.updateProfilePhotoUrl(fileStorageService.storeBase64File(request.getProfilePhoto(), "png"));
        }

        if (request.getSocialLinks() != null) {
            profile.setSocialLinks(request.getSocialLinks().stream()
                    .map(dto -> new SocialLink(dto.getPlatform(), dto.getUrl()))
                    .collect(Collectors.toList()));
        }

        if (request.getLinks() != null) {
            profile.setLinks(request.getLinks().stream()
                    .map(dto -> new Link(dto.getTitle(), dto.getUrl()))
                    .collect(Collectors.toList()));
        }

        List<Attachment> attachments = new ArrayList<>();
        
        // Process attachment files if present
        if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
            for (int i = 0; i < attachmentFiles.size(); i++) {
                MultipartFile file = attachmentFiles.get(i);
                if (file != null && !file.isEmpty()) {
                    String extension = getFileExtension(file.getOriginalFilename());
                    MediaType mediaType = getMediaTypeFromExtension(extension);
                    
                    // If there's metadata in the request, use it
                    String title = "Attachment " + (i + 1);
                    String description = "Uploaded file " + file.getOriginalFilename();
                    if (request.getAttachments() != null && i < request.getAttachments().size()) {
                        AttachmentDto metadata = request.getAttachments().get(i);
                        title = metadata.getTitle();
                        description = metadata.getDescription();
                        mediaType = metadata.getMediaType();
                    }
                    
                    attachments.add(new Attachment(
                        title,
                        description,
                        mediaType,
                        fileStorageService.storeFile(file)
                    ));
                }
            }
        }
        // Only process base64 attachments if no files were provided
        else if (request.getAttachments() != null) {
            attachments.addAll(request.getAttachments().stream()
                .filter(dto -> dto.getData() != null && !dto.getData().isEmpty())
                .map(dto -> new Attachment(
                    dto.getTitle(),
                    dto.getDescription(),
                    dto.getMediaType(),
                    fileStorageService.storeBase64File(dto.getData(), getExtensionForMediaType(dto.getMediaType()))
                ))
                .collect(Collectors.toList()));
        }
        
        profile.setAttachments(attachments);

        return toInfluencerProfileResponse(influencerProfileRepository.save(profile));
    }

    private User getCurrentUser() {
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private void validateUserRole(User user, UserRole expectedRole) {
        if (user.getRole() != expectedRole) {
            throw new ProfileException("User role mismatch: expected " + expectedRole + " but was " + user.getRole());
        }
    }

    private void validateProfileDoesNotExist(Long userId) {
        if (brandProfileRepository.existsByUserId(userId) || 
            influencerProfileRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException("Profile already exists for user ID: " + userId);
        }
    }

    private String getExtensionForMediaType(com.influmatch.profile.domain.model.valueobject.MediaType mediaType) {
        return switch (mediaType) {
            case PHOTO -> "png";
            case VIDEO -> "mp4";
            case DOCUMENT -> "pdf";
        };
    }

    private MediaType getMediaTypeFromExtension(String extension) {
        if (extension == null) return MediaType.DOCUMENT;
        
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg", "png", "gif" -> MediaType.PHOTO;
            case "mp4", "mov", "avi" -> MediaType.VIDEO;
            default -> MediaType.DOCUMENT;
        };
    }

    private String getFileExtension(String filename) {
        if (filename == null) return null;
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) return null;
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private BrandProfileResponse toBrandProfileResponse(BrandProfile profile) {
        return BrandProfileResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .sector(profile.getSector())
                .country(profile.getCountry().getValue())
                .description(profile.getDescription())
                .logo(fileStorageService.readFileAsBase64(profile.getLogoUrl()))
                .profilePhoto(fileStorageService.readFileAsBase64(profile.getProfilePhotoUrl()))
                .websiteUrl(profile.getWebsiteUrl())
                .location(profile.getLocation())
                .links(profile.getLinks().stream()
                        .map(link -> new LinkDto(link.getTitle(), link.getUrl()))
                        .collect(Collectors.toList()))
                .attachments(profile.getAttachments().stream()
                        .map(attachment -> AttachmentDto.builder()
                                .title(attachment.getTitle())
                                .description(attachment.getDescription())
                                .mediaType(attachment.getMediaType())
                                .data(fileStorageService.readFileAsBase64(attachment.getMediaUrl()))
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private InfluencerProfileResponse toInfluencerProfileResponse(InfluencerProfile profile) {
        return InfluencerProfileResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .niches(profile.getNiches().stream().toList())
                .bio(profile.getBio())
                .country(profile.getCountry().getValue())
                .photo(fileStorageService.readFileAsBase64(profile.getPhotoUrl()))
                .profilePhoto(fileStorageService.readFileAsBase64(profile.getProfilePhotoUrl()))
                .followers(profile.getFollowers())
                .socialLinks(profile.getSocialLinks().stream()
                        .map(link -> new SocialLinkDto(link.getPlatform(), link.getUrl()))
                        .collect(Collectors.toList()))
                .location(profile.getLocation())
                .links(profile.getLinks().stream()
                        .map(link -> new LinkDto(link.getTitle(), link.getUrl()))
                        .collect(Collectors.toList()))
                .attachments(profile.getAttachments().stream()
                        .map(attachment -> AttachmentDto.builder()
                                .title(attachment.getTitle())
                                .description(attachment.getDescription())
                                .mediaType(attachment.getMediaType())
                                .data(fileStorageService.readFileAsBase64(attachment.getMediaUrl()))
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
} 