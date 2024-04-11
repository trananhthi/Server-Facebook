package com.example.trananhthi.service;

import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public Page<ChatMessage> getChatMessages(Long chatRoomId, Pageable pageable) {
        return chatMessageRepository.findChatMessagesByRoomId(chatRoomId, pageable);
    }

    public ChatMessage getLastMessage(Long chatRoomId) {
        Optional<ChatMessage> chatMessages = chatMessageRepository.findFirstByRoomIdOrderByCreatedAtDesc(chatRoomId);
        return chatMessages.orElse(null);
    }
}
