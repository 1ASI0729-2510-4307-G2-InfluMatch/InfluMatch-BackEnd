package com.influmatch.collaboration.application;

import com.influmatch.collaboration.api.CreateCollaborationRequest;
import com.influmatch.collaboration.api.UpdateCollaborationRequest;
import com.influmatch.collaboration.application.exceptions.CollaborationRequestNotFoundException;
import com.influmatch.collaboration.application.exceptions.DuplicateCollaborationRequestException;
import com.influmatch.collaboration.application.exceptions.NotAuthorizedCollaborationException;
import com.influmatch.collaboration.domain.model.CollaborationRequest;
import com.influmatch.collaboration.domain.model.CollaborationStatus;
import com.influmatch.collaboration.domain.repository.CollaborationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaborationService {

    private final CollaborationRequestRepository collaborationRepo;

    @Transactional(readOnly = true)
    public List<CollaborationRequest> getRequestsByUser(Long userId, boolean received) {
        return received 
            ? collaborationRepo.findByToUserId(userId)
            : collaborationRepo.findByFromUserId(userId);
    }

    @Transactional(readOnly = true)
    public CollaborationRequest getRequestById(Long id) {
        return collaborationRepo.findById(id)
            .orElseThrow(CollaborationRequestNotFoundException::new);
    }

    @Transactional
    public CollaborationRequest createRequest(CreateCollaborationRequest request, Long fromUserId) {
        // Verificar que no exista una solicitud pendiente
        if (collaborationRepo.existsByFromUserIdAndToUserIdAndStatus(
                fromUserId, request.toUserId(), CollaborationStatus.PENDING)) {
            throw new DuplicateCollaborationRequestException();
        }

        // Crear nueva solicitud
        CollaborationRequest collab = new CollaborationRequest();
        collab.setFromUserId(fromUserId);
        collab.setToUserId(request.toUserId());
        collab.setMessage(request.message());
        collab.setStatus(CollaborationStatus.PENDING);

        return collaborationRepo.save(collab);
    }

    @Transactional
    public CollaborationRequest updateRequestStatus(
            Long requestId, 
            UpdateCollaborationRequest request, 
            Long userId) {
        
        CollaborationRequest collab = getRequestById(requestId);

        // Validar autorización según la acción
        switch (request.status()) {
            case ACCEPTED, DECLINED -> {
                if (!collab.getToUserId().equals(userId)) {
                    throw new NotAuthorizedCollaborationException();
                }
            }
            case CANCELED -> {
                if (!collab.getFromUserId().equals(userId)) {
                    throw new NotAuthorizedCollaborationException();
                }
            }
            default -> throw new IllegalArgumentException("invalid_status");
        }

        // Actualizar estado
        collab.setStatus(request.status());
        return collaborationRepo.save(collab);
    }

    @Transactional
    public void deleteRequest(Long requestId, Long userId) {
        CollaborationRequest collab = getRequestById(requestId);

        // Solo el remitente puede eliminar la solicitud
        if (!collab.getFromUserId().equals(userId)) {
            throw new NotAuthorizedCollaborationException();
        }

        collaborationRepo.delete(collab);
    }
} 