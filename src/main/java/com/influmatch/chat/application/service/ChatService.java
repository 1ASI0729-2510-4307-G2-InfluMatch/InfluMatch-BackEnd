package com.influmatch.chat.application.service;

import com.influmatch.chat.application.dto.ChatListResponseDto;
import com.influmatch.chat.application.dto.MessageResponseDto;
import com.influmatch.chat.application.dto.SendMessageRequestDto;

import java.util.List;

public interface ChatService {
    List<ChatListResponseDto> listChats();
    MessageResponseDto sendMessage(Long receiverId, SendMessageRequestDto request);
    List<MessageResponseDto> getChatMessages(Long chatId);
} 