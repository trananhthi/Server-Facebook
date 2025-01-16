package com.example.trananhthi.service.impl;

import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.repository.ChatMessageRepository;
import com.example.trananhthi.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public Page<ChatMessage> getChatMessages(String chatRoomId, Pageable pageable) {
        return chatMessageRepository.findChatMessagesByRoomId(chatRoomId, pageable);
    }

    @Override
    public ChatMessage getLastMessage(String chatRoomId) {
        Optional<ChatMessage> chatMessages = chatMessageRepository.findFirstByRoomIdOrderByCreatedAtDesc(chatRoomId);
        return chatMessages.orElse(null);
    }
}
