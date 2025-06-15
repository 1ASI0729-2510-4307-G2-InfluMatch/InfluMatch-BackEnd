package com.influmatch.dashboard.application.service;

import com.influmatch.dashboard.application.dto.*;
import com.influmatch.shared.application.dto.AttachmentDto;
import com.influmatch.shared.application.dto.LinkDto;
import com.influmatch.shared.application.dto.SocialLinkDto;
import com.influmatch.profile.domain.model.entity.BrandProfile;
import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.domain.repository.BrandProfileRepository;
import com.influmatch.profile.domain.repository.InfluencerProfileRepository;
import com.influmatch.profile.domain.exception.ProfileNotFoundException;
import com.influmatch.profile.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final BrandProfileRepository brandProfileRepository;
    private final InfluencerProfileRepository influencerProfileRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<DashboardInfluencerListDto> listInfluencers(int page, int size) {
        return influencerProfileRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(this::toInfluencerListDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DashboardBrandListDto> listBrands(int page, int size) {
        return brandProfileRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(this::toBrandListDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DashboardInfluencerDetailDto getInfluencerDetail(Long id) {
        InfluencerProfile profile = influencerProfileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Influencer profile not found with ID: " + id));
        return toInfluencerDetailDto(profile);
    }

    @Transactional(readOnly = true)
    public DashboardBrandDetailDto getBrandDetail(Long id) {
        // First try to find by profile ID
        BrandProfile profile = brandProfileRepository.findById(id)
                .orElse(null);
        
        // If not found, try to find by user ID
        if (profile == null) {
            profile = brandProfileRepository.findByUserId(id)
                    .orElseThrow(() -> new ProfileNotFoundException("Brand profile not found with ID: " + id));
        }
        
        return toBrandDetailDto(profile);
    }

    @Transactional(readOnly = true)
    public DashboardBrandDetailDto getBrandDetailByUserId(Long userId) {
        BrandProfile profile = brandProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Brand profile not found with user ID: " + userId));
        return toBrandDetailDto(profile);
    }

    private DashboardBrandListDto toBrandListDto(BrandProfile profile) {
        return DashboardBrandListDto.builder()
                .userId(profile.getUserId())
                .tradeName(profile.getName())
                .logoUrl(fileStorageService.readFileAsBase64(profile.getLogoUrl()))
                .country(profile.getCountry().getValue())
                .sector(profile.getSector())
                .build();
    }

    private DashboardInfluencerListDto toInfluencerListDto(InfluencerProfile profile) {
        return DashboardInfluencerListDto.builder()
                .userId(profile.getUserId())
                .name(profile.getName())
                .bio(profile.getBio())
                .photoUrl(fileStorageService.readFileAsBase64(profile.getPhotoUrl()))
                .country(profile.getCountry().getValue())
                .mainNiche(profile.getNiches().stream().findFirst().orElse(null))
                .followersCount(profile.getFollowers().longValue())
                .build();
    }

    private DashboardBrandDetailDto toBrandDetailDto(BrandProfile profile) {
        return DashboardBrandDetailDto.builder()
                .id(profile.getId())
                .name(profile.getName())
                .sector(profile.getSector())
                .description(profile.getDescription())
                .logo(fileStorageService.readFileAsBase64(profile.getLogoUrl()))
                .profilePhoto(fileStorageService.readFileAsBase64(profile.getProfilePhotoUrl()))
                .country(profile.getCountry().getValue())
                .websiteUrl(profile.getWebsiteUrl())
                .location(profile.getLocation())
                .links(profile.getLinks().stream()
                        .map(link -> LinkDto.builder()
                                .title(link.getTitle())
                                .url(link.getUrl())
                                .build())
                        .collect(Collectors.toList()))
                .attachments(profile.getAttachments().stream()
                        .map(attachment -> AttachmentDto.builder()
                                .title(attachment.getTitle())
                                .description(attachment.getDescription())
                                .mediaType(attachment.getMediaType().toString())
                                .data(fileStorageService.readFileAsBase64(attachment.getMediaUrl()))
                                .build())
                        .collect(Collectors.toList()))
                .rating(0.0f)
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private DashboardInfluencerDetailDto toInfluencerDetailDto(InfluencerProfile profile) {
        return DashboardInfluencerDetailDto.builder()
                .id(profile.getId())
                .name(profile.getName())
                .niches(profile.getNiches().stream().toList())
                .bio(profile.getBio())
                .country(profile.getCountry().getValue())
                .photo(fileStorageService.readFileAsBase64(profile.getPhotoUrl()))
                .profilePhoto(fileStorageService.readFileAsBase64(profile.getProfilePhotoUrl()))
                .followers(profile.getFollowers())
                .socialLinks(profile.getSocialLinks().stream()
                        .map(link -> SocialLinkDto.builder()
                                .platform(link.getPlatform())
                                .url(link.getUrl())
                                .build())
                        .collect(Collectors.toList()))
                .location(profile.getLocation())
                .links(profile.getLinks().stream()
                        .map(link -> LinkDto.builder()
                                .title(link.getTitle())
                                .url(link.getUrl())
                                .build())
                        .collect(Collectors.toList()))
                .attachments(profile.getAttachments().stream()
                        .map(attachment -> AttachmentDto.builder()
                                .title(attachment.getTitle())
                                .description(attachment.getDescription())
                                .mediaType(attachment.getMediaType().toString())
                                .data(fileStorageService.readFileAsBase64(attachment.getMediaUrl()))
                                .build())
                        .collect(Collectors.toList()))
                .rating(0.0f)
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
} 