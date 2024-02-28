package com.example.trananhthi.repository;

import com.example.trananhthi.entity.ChatMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findChatMessagesByRoomId(Long chatRoomId);
}
