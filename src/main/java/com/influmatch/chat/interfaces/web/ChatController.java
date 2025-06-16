package com.influmatch.chat.interfaces.web;

import com.influmatch.chat.application.dto.ChatListResponseDto;
import com.influmatch.chat.application.dto.ChatMessagesResponseDto;
import com.influmatch.chat.application.dto.MessageResponseDto;
import com.influmatch.chat.application.dto.SendMessageRequestDto;
import com.influmatch.chat.application.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "Chats", description = "Chat management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @Operation(summary = "List user's chats")
    public ResponseEntity<List<ChatListResponseDto>> listChats() {
        return ResponseEntity.ok(chatService.listChats());
    }

    @PostMapping("/messages/{receiverId}")
    @Operation(summary = "Send a message to a user")
    public ResponseEntity<MessageResponseDto> sendMessage(
            @PathVariable Long receiverId,
            @RequestBody SendMessageRequestDto request) {
        return ResponseEntity.ok(chatService.sendMessage(receiverId, request));
    }

    @GetMapping("/{userId}/messages")
    @Operation(summary = "Get messages from a chat with a specific user")
    public ResponseEntity<ChatMessagesResponseDto> getChatMessages(
            @PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getChatMessages(userId));
    }
} 