package com.example.trananhthi.repository;

import com.example.trananhthi.entity.ChatRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
    List<ChatRoom> findChatRoomByUserId1OrUserId2OrderByCreatedAtDesc(Long userId1, Long userId2);
    @Query("SELECT c FROM ChatRoom c WHERE (:userId1 = c.userId1 AND :userId2 = c.userId2) OR (:userId1 = c.userId2 AND :userId2 = c.userId1)")
    Optional<ChatRoom> findChatRoomByUserId1AndUserId2(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
