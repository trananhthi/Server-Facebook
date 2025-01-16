package com.example.trananhthi.service;

import com.example.trananhthi.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ChatRoomService {
    ChatRoom createChatRoom(String userId1, String userId2);

    ChatRoom getChatRoom(String userId1, String userId2);

    Page<ChatRoom> getChatRoomByUserId(String userId, Pageable pageable);

    ChatRoom getChatRoomById(String roomId);

    void updateLastMessageTime(String roomId, Date lastMessageTime);
}
