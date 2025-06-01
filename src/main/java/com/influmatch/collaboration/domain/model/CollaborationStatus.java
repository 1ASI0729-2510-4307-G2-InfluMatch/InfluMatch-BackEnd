package com.influmatch.collaboration.domain.model;

public enum CollaborationStatus {
    PENDING,    // Solicitud enviada, esperando respuesta
    ACCEPTED,   // Solicitud aceptada por el destinatario
    DECLINED,   // Solicitud rechazada por el destinatario
    CANCELED    // Solicitud cancelada por el remitente
} 