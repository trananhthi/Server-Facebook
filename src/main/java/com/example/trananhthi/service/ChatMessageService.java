package com.example.trananhthi.service;

import com.example.trananhthi.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageService {

    Page<ChatMessage> getChatMessages(String chatRoomId, Pageable pageable);

    void processMessage(ChatMessage chatMessage);
}
