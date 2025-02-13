package com.example.trananhthi.repository;

import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.enumtype.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    Page<ChatMessage> findChatMessagesByRoomIdAndStatus(String chatRoomId, Status status, Pageable pageable);

    Optional<ChatMessage> findFirstByRoomIdAndStatusOrderByCreatedAtDesc(String chatRoomId, Status status);
}