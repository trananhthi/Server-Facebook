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
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    public void processMessage(ChatMessage chatMessage, SimpMessageHeaderAccessor accessor) {
        String userId = (String) Objects.requireNonNull(accessor.getSessionAttributes()).get("userId");
        ChatMessage savedMsg = chatMessageRepository.save(chatMessage);
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getRoomId()).orElse(null);
        if (chatRoom != null)
        {
            chatRoom.setLastMessageTime(chatMessage.getCreatedAt());
            chatRoomRepository.save(chatRoom);
            simpMessagingTemplate.convertAndSendToUser(Objects.equals(userId, chatRoom.getUserId1()) ? chatRoom.getUserId2() : chatRoom.getUserId1(),
                    "/queue/messages" , savedMsg );
        }
        else
        {
            throw new RuntimeException("Chat room not found");
        }

    }
}
