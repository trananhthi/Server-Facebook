package com.example.trananhthi.service;

import com.example.trananhthi.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageService {
    ChatMessage save(ChatMessage chatMessage);

    Page<ChatMessage> getChatMessages(String chatRoomId, Pageable pageable);

    ChatMessage getLastMessage(String chatRoomId);
}
