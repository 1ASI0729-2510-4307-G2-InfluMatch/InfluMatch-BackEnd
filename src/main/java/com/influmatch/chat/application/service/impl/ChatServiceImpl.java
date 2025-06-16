package com.influmatch.chat.application.service.impl;

import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.UserRole;
import com.influmatch.auth.domain.repository.UserRepository;
import com.influmatch.chat.application.dto.ChatListResponseDto;
import com.influmatch.chat.application.dto.ChatMessagesResponseDto;
import com.influmatch.chat.application.dto.MessageDetailDto;
import com.influmatch.chat.application.dto.MessageResponseDto;
import com.influmatch.chat.application.dto.SendMessageRequestDto;
import com.influmatch.chat.application.service.ChatService;
import com.influmatch.chat.domain.model.entity.Chat;
import com.influmatch.chat.domain.model.entity.Message;
import com.influmatch.chat.domain.model.repository.ChatRepository;
import com.influmatch.chat.domain.model.valueobject.AttachmentType;
import com.influmatch.chat.infrastructure.storage.FileStorageService;
import com.influmatch.profile.domain.model.entity.BrandProfile;
import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.domain.repository.BrandProfileRepository;
import com.influmatch.profile.domain.repository.InfluencerProfileRepository;
import com.influmatch.shared.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final BrandProfileRepository brandProfileRepository;
    private final InfluencerProfileRepository influencerProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChatListResponseDto> listChats() {
        Long userId = securityUtils.getCurrentUserId();
        return chatRepository.findChatsByUserId(userId).stream()
                .map(this::toChatListResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponseDto sendMessage(Long receiverId, SendMessageRequestDto request) {
        Long senderId = securityUtils.getCurrentUserId();
        
        // Find existing chat or create a new one
        Chat chat = findOrCreateChat(senderId, receiverId);

        String attachmentUrl = null;
        if (request.getAttachmentBase64() != null && request.getAttachmentType() != null) {
            // Validate base64 string
            String base64Data = request.getAttachmentBase64().trim();
            if (!isValidBase64(base64Data)) {
                throw new IllegalArgumentException("Invalid base64 format for attachment");
            }

            try {
                byte[] fileBytes = Base64.getDecoder().decode(base64Data);
                String mimeType = getMimeType(request.getAttachmentType());
                attachmentUrl = fileStorageService.storeFile(fileBytes, mimeType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to decode base64 attachment: " + e.getMessage());
            }
        }

        Message message = Message.builder()
                .chatId(chat.getChatId())
                .senderId(senderId)
                .receiverId(receiverId)
                .content(request.getContent())
                .attachmentUrl(attachmentUrl)
                .createdAt(Instant.now())
                .build();

        message = chatRepository.saveMessage(message);

        // Increment unread count for the receiver
        chatRepository.updateUnreadCount(chat.getChatId(), receiverId, chat.getUnreadCount() + 1);

        return toMessageResponseDto(message);
    }

    private boolean isValidBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return false;
        }

        // Check if length is valid (must be multiple of 4)
        if (base64String.length() % 4 != 0) {
            return false;
        }

        // Check if string contains only valid base64 characters
        return base64String.matches("^[A-Za-z0-9+/]*={0,2}$");
    }

    private String getMimeType(AttachmentType type) {
        return switch (type) {
            case PHOTO -> "image/jpeg";
            case VIDEO -> "video/mp4";
            case DOCUMENT -> "application/pdf";
        };
    }

    @Override
    @Transactional
    public ChatMessagesResponseDto getChatMessages(Long userId) {
        Long currentUserId = securityUtils.getCurrentUserId();
        
        // Find the chat between current user and the specified user, or create one if it doesn't exist
        Chat chat = chatRepository.findChatsByUserId(currentUserId).stream()
                .filter(c -> c.getInterlocutorId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    // Create a new chat if none exists (similar to sendMessage behavior)
                    Chat newChat = Chat.builder()
                            .userId(currentUserId)
                            .interlocutorId(userId)
                            .unreadCount(0)
                            .build();
                    return chatRepository.save(newChat);
                });

        // Reset unread count for the current user
        if (chat.getUnreadCount() > 0) {
            chatRepository.updateUnreadCount(chat.getChatId(), currentUserId, 0);
        }

        // Get interlocutor information
        User interlocutor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Interlocutor not found"));

        String interlocutorName;
        String interlocutorPhotoUrl;

        if (interlocutor.getRole() == UserRole.BRAND) {
            BrandProfile profile = brandProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Brand profile not found"));
            interlocutorName = profile.getName();
            interlocutorPhotoUrl = profile.getProfilePhotoUrl() != null ? profile.getProfilePhotoUrl() : profile.getLogoUrl();
        } else {
            InfluencerProfile profile = influencerProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Influencer profile not found"));
            interlocutorName = profile.getName();
            interlocutorPhotoUrl = profile.getProfilePhotoUrl() != null ? profile.getProfilePhotoUrl() : profile.getPhotoUrl();
        }

        String interlocutorPhotoBase64 = null;
        if (interlocutorPhotoUrl != null) {
            interlocutorPhotoBase64 = fileStorageService.readFileAsBase64(interlocutorPhotoUrl);
        }

        // Get messages with sender details
        List<MessageDetailDto> messages = chatRepository.findMessagesByChatId(chat.getChatId()).stream()
                .map(message -> toMessageDetailDto(message, currentUserId))
                .collect(Collectors.toList());

        return ChatMessagesResponseDto.builder()
                .interlocutor(ChatMessagesResponseDto.InterlocutorInfo.builder()
                        .userId(userId)
                        .name(interlocutorName)
                        .photoBase64(interlocutorPhotoBase64)
                        .build())
                .messages(messages)
                .build();
    }

    private Chat findOrCreateChat(Long userId, Long interlocutorId) {
        // Try to find an existing chat between these users
        Optional<Chat> existingChat = chatRepository.findChatsByUserId(userId).stream()
                .filter(chat -> chat.getInterlocutorId().equals(interlocutorId))
                .findFirst();

        if (existingChat.isPresent()) {
            return existingChat.get();
        }

        // Create a new chat if none exists
        Chat newChat = Chat.builder()
                .userId(userId)
                .interlocutorId(interlocutorId)
                .unreadCount(0)
                .build();

        return chatRepository.save(newChat);
    }

    private ChatListResponseDto toChatListResponseDto(Chat chat) {
        // Get interlocutor information
        User interlocutor = userRepository.findById(chat.getInterlocutorId())
                .orElseThrow(() -> new RuntimeException("Interlocutor not found"));

        // Get interlocutor's name and photo based on their role
        String name;
        String photoUrl;

        if (interlocutor.getRole() == UserRole.BRAND) {
            BrandProfile profile = brandProfileRepository.findByUserId(chat.getInterlocutorId())
                    .orElseThrow(() -> new RuntimeException("Brand profile not found"));
            name = profile.getName();
            photoUrl = profile.getProfilePhotoUrl() != null ? profile.getProfilePhotoUrl() : profile.getLogoUrl();
        } else {
            InfluencerProfile profile = influencerProfileRepository.findByUserId(chat.getInterlocutorId())
                    .orElseThrow(() -> new RuntimeException("Influencer profile not found"));
            name = profile.getName();
            photoUrl = profile.getProfilePhotoUrl() != null ? profile.getProfilePhotoUrl() : profile.getPhotoUrl();
        }

        // Convert photo to base64 if it exists
        String photoBase64 = null;
        if (photoUrl != null) {
            photoBase64 = fileStorageService.readFileAsBase64(photoUrl);
        }

        return ChatListResponseDto.builder()
                .chatId(chat.getChatId())
                .interlocutorId(chat.getInterlocutorId())
                .interlocutor(ChatListResponseDto.InterlocutorInfo.builder()
                        .id(chat.getInterlocutorId())
                        .name(name)
                        .photoBase64(photoBase64)
                        .build())
                .lastMessage(chat.getLastMessage() != null ? toLastMessageInfo(chat.getLastMessage()) : null)
                .unreadCount(chat.getUnreadCount())
                .build();
    }

    private ChatListResponseDto.LastMessageInfo toLastMessageInfo(Message message) {
        return ChatListResponseDto.LastMessageInfo.builder()
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private MessageResponseDto toMessageResponseDto(Message message) {
        return MessageResponseDto.builder()
                .messageId(message.getMessageId())
                .chatId(message.getChatId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .attachmentUrl(message.getAttachmentUrl())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private MessageDetailDto toMessageDetailDto(Message message, Long currentUserId) {
        // Get sender information
        User sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        String senderName;
        String senderPhotoUrl;

        if (sender.getRole() == UserRole.BRAND) {
            BrandProfile profile = brandProfileRepository.findByUserId(message.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Brand profile not found"));
            senderName = profile.getName();
            senderPhotoUrl = profile.getProfilePhotoUrl() != null ? profile.getProfilePhotoUrl() : profile.getLogoUrl();
        } else {
            InfluencerProfile profile = influencerProfileRepository.findByUserId(message.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Influencer profile not found"));
            senderName = profile.getName();
            senderPhotoUrl = profile.getProfilePhotoUrl() != null ? profile.getProfilePhotoUrl() : profile.getPhotoUrl();
        }

        String senderPhotoBase64 = null;
        if (senderPhotoUrl != null) {
            senderPhotoBase64 = fileStorageService.readFileAsBase64(senderPhotoUrl);
        }

        return MessageDetailDto.builder()
                .messageId(message.getMessageId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .attachmentUrl(message.getAttachmentUrl())
                .createdAt(message.getCreatedAt())
                .isFromMe(message.getSenderId().equals(currentUserId))
                .senderName(senderName)
                .senderPhoto(senderPhotoBase64)
                .build();
    }
} 