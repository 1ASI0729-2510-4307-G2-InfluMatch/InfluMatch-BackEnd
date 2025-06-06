package com.influmatch.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // Prefijo para canales de broadcast
        config.setApplicationDestinationPrefixes("/app");  // Prefijo para endpoints de aplicación
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")  // Endpoint para conexión WebSocket
            .setAllowedOriginPatterns("*")  // Permitir conexiones desde cualquier origen
            .withSockJS();  // Habilitar fallback a polling si WebSocket no está disponible
    }
} 