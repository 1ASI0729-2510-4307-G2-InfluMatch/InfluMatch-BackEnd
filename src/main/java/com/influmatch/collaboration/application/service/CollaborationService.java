package com.influmatch.collaboration.application.service;

import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.UserRole;
import com.influmatch.auth.domain.repository.UserRepository;
import com.influmatch.auth.infrastructure.security.CurrentUser;
import com.influmatch.collaboration.application.dto.*;
import com.influmatch.collaboration.domain.model.entity.Collaboration;
import com.influmatch.collaboration.domain.model.valueobject.ActionType;
import com.influmatch.collaboration.domain.model.valueobject.CollaborationStatus;
import com.influmatch.collaboration.domain.model.valueobject.Milestone;
import com.influmatch.collaboration.domain.repository.CollaborationRepository;
import com.influmatch.profile.application.service.FileStorageService;
import com.influmatch.profile.domain.model.entity.BrandProfile;
import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.domain.repository.BrandProfileRepository;
import com.influmatch.profile.domain.repository.InfluencerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollaborationService {
    private final CollaborationRepository collaborationRepository;
    private final UserRepository userRepository;
    private final BrandProfileRepository brandProfileRepository;
    private final InfluencerProfileRepository influencerProfileRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public CollaborationDetailDto createCollaboration(CreateCollaborationRequest request) {
        User currentUser = getCurrentUser();
        User counterpart = userRepository.findById(request.getCounterpartId())
                .orElseThrow(() -> new IllegalArgumentException("Counterpart not found"));

        validateRoles(currentUser, counterpart);

        Collaboration collaboration = new Collaboration(
                currentUser.getId(),
                counterpart.getId(),
                currentUser.getRole(),
                request.getMessage(),
                ActionType.valueOf(request.getActionType()),
                LocalDate.parse(request.getTargetDate()),
                request.getBudget(),
                request.getMilestones().stream()
                        .map(dto -> new Milestone(
                                dto.getTitle(),
                                LocalDate.parse(dto.getDate()),
                                dto.getDescription(),
                                dto.getLocation(),
                                dto.getDeliverables()
                        ))
                        .collect(Collectors.toList()),
                request.getLocation(),
                request.getDeliverables()
        );

        Collaboration savedCollaboration = collaborationRepository.save(collaboration);
        return toCollaborationDetailDto(savedCollaboration);
    }

    @Transactional(readOnly = true)
    public List<CollaborationListDto> listCollaborations(CollaborationStatus status) {
        User currentUser = getCurrentUser();
        List<Collaboration> collaborations = status != null ?
                collaborationRepository.findAllByUserAndStatus(currentUser.getId(), status) :
                collaborationRepository.findAllByUser(currentUser.getId());

        return collaborations.stream()
                .map(collaboration -> toCollaborationListDto(collaboration, currentUser.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CollaborationDetailDto getCollaboration(Long id) {
        User currentUser = getCurrentUser();
        Collaboration collaboration = findCollaborationAndValidateAccess(id, currentUser);
        return toCollaborationDetailDto(collaboration);
    }

    @Transactional
    public CollaborationDetailDto updateCollaborationStatus(Long id, UpdateCollaborationStatusRequest request) {
        User currentUser = getCurrentUser();
        Collaboration collaboration = findCollaborationAndValidateAccess(id, currentUser);

        switch (request.getAction().toUpperCase()) {
            case "ACCEPT" -> {
                validateIsRecipient(collaboration, currentUser);
                collaboration.accept();
            }
            case "REJECT" -> {
                validateIsRecipient(collaboration, currentUser);
                collaboration.reject();
            }
            case "CANCEL" -> {
                validateIsInitiator(collaboration, currentUser);
                collaboration.cancel();
            }
            case "FINISH" -> {
                validateIsInitiator(collaboration, currentUser);
                collaboration.finish();
            }
            default -> throw new IllegalArgumentException("Invalid action: " + request.getAction());
        }

        return toCollaborationDetailDto(collaborationRepository.save(collaboration));
    }

    @Transactional
    public CollaborationDetailDto updateCollaboration(Long id, UpdateCollaborationRequest request) {
        User currentUser = getCurrentUser();
        Collaboration collaboration = findCollaborationAndValidateAccess(id, currentUser);
        validateIsInitiator(collaboration, currentUser);

        collaboration.update(
                request.getMessage(),
                request.getTargetDate() != null ? LocalDate.parse(request.getTargetDate()) : null,
                request.getActionType() != null ? ActionType.valueOf(request.getActionType()) : null,
                request.getBudget(),
                request.getMilestones() != null ? request.getMilestones().stream()
                        .map(dto -> new Milestone(
                                dto.getTitle(),
                                LocalDate.parse(dto.getDate()),
                                dto.getDescription(),
                                dto.getLocation(),
                                dto.getDeliverables()
                        ))
                        .collect(Collectors.toList()) : null,
                request.getLocation(),
                request.getDeliverables()
        );

        return toCollaborationDetailDto(collaborationRepository.save(collaboration));
    }

    @Transactional(readOnly = true)
    public List<AgendaEventDto> getAgenda() {
        User currentUser = getCurrentUser();
        List<Collaboration> acceptedCollaborations = collaborationRepository.findAcceptedByUser(currentUser.getId());

        return acceptedCollaborations.stream()
                .flatMap(collaboration -> collaboration.getMilestones().stream()
                        .map(milestone -> AgendaEventDto.builder()
                                .collaborationId(collaboration.getId())
                                .initiatorId(collaboration.getInitiatorId())
                                .counterpartId(collaboration.getCounterpartId())
                                .date(milestone.getDate().toString())
                                .eventTitle(milestone.getTitle())
                                .description(milestone.getDescription())
                                .location(milestone.getLocation())
                                .counterpartName(getCounterpartName(collaboration, currentUser.getId()))
                                .build()))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private void validateRoles(User initiator, User counterpart) {
        if (initiator.getRole() == counterpart.getRole()) {
            throw new IllegalArgumentException("Cannot collaborate with user of the same role");
        }
    }

    private Collaboration findCollaborationAndValidateAccess(Long id, User user) {
        Collaboration collaboration = collaborationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Collaboration not found"));

        if (!collaboration.getInitiatorId().equals(user.getId()) && !collaboration.getCounterpartId().equals(user.getId())) {
            throw new IllegalStateException("User is not part of this collaboration");
        }

        return collaboration;
    }

    private void validateIsInitiator(Collaboration collaboration, User user) {
        if (!collaboration.getInitiatorId().equals(user.getId())) {
            throw new IllegalStateException("Only the initiator can perform this action");
        }
    }

    private void validateIsRecipient(Collaboration collaboration, User user) {
        if (!collaboration.getCounterpartId().equals(user.getId())) {
            throw new IllegalStateException("Only the recipient can perform this action");
        }
    }

    private CollaborationListDto toCollaborationListDto(Collaboration collaboration, Long currentUserId) {
        return CollaborationListDto.builder()
                .id(collaboration.getId())
                .initiatorId(collaboration.getInitiatorId())
                .counterpartId(collaboration.getCounterpartId())
                .initiatorRole(collaboration.getInitiatorRole().toString())
                .status(collaboration.getStatus().toString())
                .counterpartName(getCounterpartName(collaboration, currentUserId))
                .counterpartPhotoUrl(getCounterpartPhotoUrl(collaboration, currentUserId))
                .message(collaboration.getMessage())
                .actionType(collaboration.getActionType().toString())
                .createdAt(collaboration.getCreatedAt())
                .build();
    }

    private CollaborationListDto toCollaborationListDto(Collaboration collaboration) {
        return toCollaborationListDto(collaboration, collaboration.getInitiatorId());
    }

    private CollaborationDetailDto toCollaborationDetailDto(Collaboration collaboration) {
        return CollaborationDetailDto.builder()
                .id(collaboration.getId())
                .initiatorId(collaboration.getInitiatorId())
                .counterpartId(collaboration.getCounterpartId())
                .status(collaboration.getStatus().toString())
                .initiatorRole(collaboration.getInitiatorRole().toString())
                .counterpart(getCounterpartInfo(collaboration))
                .message(collaboration.getMessage())
                .actionType(collaboration.getActionType().toString())
                .targetDate(collaboration.getTargetDate().toString())
                .budget(collaboration.getBudget())
                .milestones(collaboration.getMilestones().stream()
                        .map(milestone -> MilestoneDto.builder()
                                .title(milestone.getTitle())
                                .date(milestone.getDate().toString())
                                .description(milestone.getDescription())
                                .location(milestone.getLocation())
                                .deliverables(milestone.getDeliverables())
                                .build())
                        .collect(Collectors.toList()))
                .location(collaboration.getLocation())
                .deliverables(collaboration.getDeliverables())
                .createdAt(collaboration.getCreatedAt())
                .updatedAt(collaboration.getUpdatedAt())
                .build();
    }

    private String getCounterpartName(Collaboration collaboration, Long currentUserId) {
        Long counterpartId;
        if (collaboration.getInitiatorId().equals(currentUserId)) {
            counterpartId = collaboration.getCounterpartId();
        } else if (collaboration.getCounterpartId().equals(currentUserId)) {
            counterpartId = collaboration.getInitiatorId();
        } else {
            throw new IllegalStateException("User is not part of this collaboration");
        }

        User counterpart = userRepository.findById(counterpartId)
                .orElseThrow(() -> new IllegalStateException("Counterpart not found"));

        if (counterpart.getRole() == UserRole.BRAND) {
            return brandProfileRepository.findByUserId(counterpartId)
                    .map(BrandProfile::getName)
                    .orElse("Unknown Brand");
        } else {
            return influencerProfileRepository.findByUserId(counterpartId)
                    .map(InfluencerProfile::getName)
                    .orElse("Unknown Influencer");
        }
    }

    private String getCounterpartPhotoUrl(Collaboration collaboration, Long currentUserId) {
        Long counterpartId;
        if (collaboration.getInitiatorId().equals(currentUserId)) {
            counterpartId = collaboration.getCounterpartId();
        } else if (collaboration.getCounterpartId().equals(currentUserId)) {
            counterpartId = collaboration.getInitiatorId();
        } else {
            throw new IllegalStateException("User is not part of this collaboration");
        }

        User counterpart = userRepository.findById(counterpartId)
                .orElseThrow(() -> new IllegalStateException("Counterpart not found"));

        if (counterpart.getRole() == UserRole.BRAND) {
            return brandProfileRepository.findByUserId(counterpartId)
                    .map(profile -> fileStorageService.readFileAsBase64(profile.getLogoUrl()))
                    .orElse(null);
        } else {
            return influencerProfileRepository.findByUserId(counterpartId)
                    .map(profile -> fileStorageService.readFileAsBase64(profile.getPhotoUrl()))
                    .orElse(null);
        }
    }

    private CounterpartDto getCounterpartInfo(Collaboration collaboration) {
        if (collaboration.getInitiatorRole() == UserRole.BRAND) {
            InfluencerProfile profile = influencerProfileRepository.findByUserId(collaboration.getCounterpartId())
                    .orElseThrow(() -> new IllegalStateException("Counterpart profile not found"));

            return CounterpartDto.builder()
                    .id(profile.getUserId())
                    .name(profile.getName())
                    .photoUrl(fileStorageService.readFileAsBase64(profile.getPhotoUrl()))
                    .build();
        } else {
            BrandProfile profile = brandProfileRepository.findByUserId(collaboration.getCounterpartId())
                    .orElseThrow(() -> new IllegalStateException("Counterpart profile not found"));

            return CounterpartDto.builder()
                    .id(profile.getUserId())
                    .name(profile.getName())
                    .photoUrl(fileStorageService.readFileAsBase64(profile.getLogoUrl()))
                    .build();
        }
    }
} 