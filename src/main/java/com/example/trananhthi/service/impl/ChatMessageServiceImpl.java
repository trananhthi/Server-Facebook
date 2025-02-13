package com.example.trananhthi.service.impl;

import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.entity.ChatRoom;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.repository.ChatMessageRepository;
import com.example.trananhthi.repository.ChatRoomRepository;
import com.example.trananhthi.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Page<ChatMessage> getChatMessages(String chatRoomId, Pageable pageable) {
        return chatMessageRepository.findChatMessagesByRoomIdAndStatus(chatRoomId, Status.ACT, pageable);
    }

    @Override
    public void processMessage(ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageRepository.save(chatMessage);
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getRoomId()).orElse(null);
        if (chatRoom != null)
        {
            chatRoom.setLastMessageTime(chatMessage.getCreatedAt());
            chatRoomRepository.save(chatRoom);
            simpMessagingTemplate.convertAndSendToUser(chatRoom.getUserId2(),"/queue/messages" , savedMsg );
            simpMessagingTemplate.convertAndSendToUser(chatRoom.getUserId1(),"/queue/messages" , savedMsg );
        }
        else
        {
            throw new RuntimeException("Chat room not found");
        }

    }
}
