package com.influmatch.collaboration.application.assembler;

import com.influmatch.collaboration.application.dto.*;
import com.influmatch.collaboration.domain.model.entity.Collaboration;
import com.influmatch.collaboration.domain.model.valueobject.ActionType;
import com.influmatch.collaboration.domain.model.valueobject.Budget;
import com.influmatch.collaboration.domain.model.valueobject.Milestone;
import com.influmatch.profile.domain.model.entity.BrandProfile;
import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CollaborationAssembler {
    private final FileStorageService fileStorageService;

    public CollaborationListDto toListDto(Collaboration collaboration, String counterpartName) {
        return CollaborationListDto.builder()
                .id(collaboration.getId())
                .initiatorRole(collaboration.getInitiatorRole().toString())
                .status(collaboration.getStatus().toString())
                .counterpartName(counterpartName)
                .message(collaboration.getMessage())
                .actionType(collaboration.getActionType().toString())
                .createdAt(collaboration.getCreatedAt())
                .build();
    }

    public CollaborationDetailDto toDetailDto(Collaboration collaboration, CounterpartDto counterpart) {
        return CollaborationDetailDto.builder()
                .id(collaboration.getId())
                .status(collaboration.getStatus().toString())
                .initiatorRole(collaboration.getInitiatorRole().toString())
                .counterpart(counterpart)
                .message(collaboration.getMessage())
                .actionType(collaboration.getActionType().toString())
                .targetDate(collaboration.getTargetDate().toString())
                .budget(collaboration.getBudget())
                .milestones(collaboration.getMilestones().stream()
                        .map(this::toMilestoneDto)
                        .collect(Collectors.toList()))
                .location(collaboration.getLocation())
                .deliverables(collaboration.getDeliverables())
                .createdAt(collaboration.getCreatedAt())
                .updatedAt(collaboration.getUpdatedAt())
                .build();
    }

    public CounterpartDto toCounterpartDto(BrandProfile profile) {
        return CounterpartDto.builder()
                .id(profile.getUserId())
                .name(profile.getName())
                .photoUrl(fileStorageService.readFileAsBase64(profile.getLogoUrl()))
                .build();
    }

    public CounterpartDto toCounterpartDto(InfluencerProfile profile) {
        return CounterpartDto.builder()
                .id(profile.getUserId())
                .name(profile.getName())
                .photoUrl(fileStorageService.readFileAsBase64(profile.getPhotoUrl()))
                .build();
    }

    public MilestoneDto toMilestoneDto(Milestone milestone) {
        return MilestoneDto.builder()
                .title(milestone.getTitle())
                .date(milestone.getDate().toString())
                .description(milestone.getDescription())
                .location(milestone.getLocation())
                .deliverables(milestone.getDeliverables())
                .build();
    }

    public AgendaEventDto toAgendaEventDto(Collaboration collaboration, Milestone milestone, String counterpartName) {
        return AgendaEventDto.builder()
                .collaborationId(collaboration.getId())
                .date(milestone.getDate().toString())
                .eventTitle(milestone.getTitle())
                .counterpartName(counterpartName)
                .build();
    }

    public Milestone toMilestone(MilestoneDto dto) {
        return new Milestone(
                dto.getTitle(),
                LocalDate.parse(dto.getDate()),
                dto.getDescription(),
                dto.getLocation(),
                dto.getDeliverables()
        );
    }

    public Budget toBudget(CreateCollaborationRequest request) {
        return Budget.of(request.getBudget());
    }

    public ActionType toActionType(String actionType) {
        return ActionType.valueOf(actionType);
    }
} 