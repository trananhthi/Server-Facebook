package com.example.trananhthi.service;

import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.model.TypingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ChatMessageService {

    Page<ChatMessage> getChatMessages(String chatRoomId, Pageable pageable);

    void processMessage(ChatMessage chatMessage, SimpMessageHeaderAccessor accessor);

    void processTyping(TypingStatus typingStatus, SimpMessageHeaderAccessor accessor);
}
