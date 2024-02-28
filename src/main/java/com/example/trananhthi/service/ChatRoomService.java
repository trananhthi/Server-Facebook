package com.example.trananhthi.service;

import com.example.trananhthi.entity.ChatRoom;
import com.example.trananhthi.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createChatRoom(Long userId1, Long userId2)
    {
        ChatRoom chatRoom = ChatRoom.builder()
                .userId1(userId1)
                .userId2(userId2)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoom(Long userId1, Long userId2)
    {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByUserId1AndUserId2(userId1,userId2);
        if (chatRoom.isEmpty()) {
            return createChatRoom(userId1,userId2);
        }
        else
        {
            return chatRoom.get();
        }
    }

    public ChatRoom getChatRoomById(Long roomId)
    {
        return chatRoomRepository.findById(roomId).orElse(null);
    }
}
