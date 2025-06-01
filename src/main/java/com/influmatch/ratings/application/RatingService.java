package com.influmatch.ratings.application;

import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.collaboration.domain.repository.CampaignRepository;
import com.influmatch.ratings.api.CreateRatingRequest;
import com.influmatch.ratings.api.UpdateRatingRequest;
import com.influmatch.ratings.domain.model.Rating;
import com.influmatch.ratings.domain.repository.RatingRepository;
import com.influmatch.shared.domain.exception.BusinessException;
import com.influmatch.shared.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final CampaignRepository campaignRepository;

    @Transactional(readOnly = true)
    public Page<Rating> getRatings(Long writerId, Long targetId, Long campaignId, Long userId, boolean isAdmin, Pageable pageable) {
        // Si es admin, puede ver todas las valoraciones con los filtros proporcionados
        if (isAdmin) {
            return ratingRepository.findByFilters(writerId, targetId, campaignId, pageable);
        }
        
        // Si no es admin, solo puede ver las valoraciones donde es writer o target
        if (writerId != null && !writerId.equals(userId)) {
            throw new BusinessException(
                "unauthorized_filter",
                "No puede filtrar por valoraciones de otros usuarios"
            );
        }

        return ratingRepository.findByFilters(
            writerId != null ? writerId : userId,
            targetId,
            campaignId,
            pageable
        );
    }

    @Transactional(readOnly = true)
    public Rating getRating(Long ratingId, Long userId, boolean isAdmin) {
        Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new NotFoundException("rating_not_found", "La valoración no existe"));

        if (!isAdmin && !canAccessRating(rating, userId)) {
            throw new BusinessException(
                "unauthorized_rating",
                "No tiene permiso para ver esta valoración"
            );
        }

        return rating;
    }

    @Transactional
    public Rating createRating(CreateRatingRequest request, Long writerId) {
        validateTargetId(request.targetId());
        Campaign campaign = null;
        
        if (request.campaignId() != null) {
            campaign = campaignRepository.findById(request.campaignId())
                .orElseThrow(() -> new NotFoundException("campaign_not_found", "La campaña no existe"));
            
            validateCampaignParticipation(campaign, writerId, request.targetId());
        }

        Rating rating = new Rating(
            writerId,
            request.targetId(),
            campaign,
            request.score(),
            request.comment()
        );

        return ratingRepository.save(rating);
    }

    @Transactional
    public Rating updateRating(Long ratingId, UpdateRatingRequest request, Long userId, boolean isAdmin) {
        Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new NotFoundException("rating_not_found", "La valoración no existe"));

        if (!isAdmin && !rating.getWriterId().equals(userId)) {
            throw new BusinessException(
                "unauthorized_rating",
                "No tiene permiso para modificar esta valoración"
            );
        }

        rating.update(request.score(), request.comment());
        return ratingRepository.save(rating);
    }

    @Transactional
    public void deleteRating(Long ratingId, Long userId, boolean isAdmin) {
        Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new NotFoundException("rating_not_found", "La valoración no existe"));

        if (!isAdmin && !rating.getWriterId().equals(userId)) {
            throw new BusinessException(
                "unauthorized_rating",
                "No tiene permiso para eliminar esta valoración"
            );
        }

        ratingRepository.delete(rating);
    }

    private boolean canAccessRating(Rating rating, Long userId) {
        return rating.getWriterId().equals(userId) || rating.getTargetId().equals(userId);
    }

    private void validateTargetId(Long targetId) {
        // Aquí deberías validar que el targetId corresponde a un usuario existente
        // Por ahora solo verificamos que no sea null, pero deberías agregar la validación real
        if (targetId == null) {
            throw new BusinessException(
                "invalid_target",
                "El ID del destinatario es inválido"
            );
        }
    }

    private void validateCampaignParticipation(Campaign campaign, Long writerId, Long targetId) {
        // Verificar que tanto writerId como targetId participaron en la campaña
        if (!isParticipantInCampaign(campaign, writerId) || 
            !isParticipantInCampaign(campaign, targetId)) {
            throw new BusinessException(
                "invalid_campaign_participants",
                "Ambos usuarios deben haber participado en la campaña"
            );
        }
    }

    private boolean isParticipantInCampaign(Campaign campaign, Long userId) {
        return userId.equals(campaign.getBrandId()) || 
               userId.equals(campaign.getInfluencerId());
    }
} 