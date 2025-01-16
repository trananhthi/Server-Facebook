package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.entity.ChatRoom;
import com.example.trananhthi.repository.ChatRoomRepository;
import com.example.trananhthi.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl extends BaseServiceImpl<ChatRoom, ChatRoomRepository> implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(String userId1, String userId2)
    {
        ChatRoom chatRoom = ChatRoom.builder()
                .userId1(userId1)
                .userId2(userId2)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom getChatRoom(String userId1, String userId2)
    {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByUserId1AndUserId2(userId1,userId2);
        return chatRoom.orElseGet(() -> createChatRoom(userId1, userId2));
    }

    @Override
    public Page<ChatRoom> getChatRoomByUserId(String userId, Pageable pageable)
    {
        return chatRoomRepository.findChatRoomByUserId1OrUserId2(userId,userId,pageable);
    }

    @Override
    public ChatRoom getChatRoomById(String roomId)
    {
        return chatRoomRepository.findById(roomId).orElse(null);
    }

    @Override
    public void updateLastMessageTime(String roomId, Date lastMessageTime)
    {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        if (chatRoom != null)
        {
            chatRoom.setLastMessageTime(lastMessageTime);
            chatRoomRepository.save(chatRoom);
        }
    }
}
