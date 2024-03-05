package com.example.trananhthi.service;

import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        return chatMessageRepository.findChatMessagesByRoomId(chatRoomId);
    }
}
