package com.influmatch.collaboration.infrastructure.web;

import com.influmatch.collaboration.application.dto.*;
import com.influmatch.collaboration.application.service.CollaborationService;
import com.influmatch.collaboration.domain.model.valueobject.CollaborationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborations")
@RequiredArgsConstructor
@Tag(name = "Collaborations", description = "Collaboration management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CollaborationController {
    private final CollaborationService collaborationService;

    @PostMapping
    @Operation(summary = "Create a new collaboration request")
    public ResponseEntity<CollaborationDetailDto> createCollaboration(
            @Valid @RequestBody CreateCollaborationRequest request) {
        return ResponseEntity.ok(collaborationService.createCollaboration(request));
    }

    @GetMapping
    @Operation(summary = "List user's collaborations")
    public ResponseEntity<Page<CollaborationListDto>> listCollaborations(
            @RequestParam(required = false) CollaborationStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(collaborationService.listCollaborations(status, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get collaboration details")
    public ResponseEntity<CollaborationDetailDto> getCollaboration(
            @PathVariable Long id) {
        return ResponseEntity.ok(collaborationService.getCollaboration(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update collaboration status")
    public ResponseEntity<CollaborationDetailDto> updateCollaborationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCollaborationStatusRequest request) {
        return ResponseEntity.ok(collaborationService.updateCollaborationStatus(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edit collaboration details")
    public ResponseEntity<CollaborationDetailDto> updateCollaboration(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCollaborationRequest request) {
        return ResponseEntity.ok(collaborationService.updateCollaboration(id, request));
    }

    @GetMapping("/agenda")
    @Operation(summary = "List accepted collaboration milestones")
    public ResponseEntity<List<AgendaEventDto>> listAgendaEvents() {
        return ResponseEntity.ok(collaborationService.getAgenda());
    }
} 