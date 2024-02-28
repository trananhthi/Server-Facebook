package com.example.trananhthi.repository;

import com.example.trananhthi.entity.ChatRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
    Optional<ChatRoom> findChatRoomByUserId1AndUserId2(Long userId1, Long userId2);
}
