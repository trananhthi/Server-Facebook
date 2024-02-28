package com.example.trananhthi.controller;

import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.entity.ChatRoom;
import com.example.trananhthi.service.ChatMessageService;
import com.example.trananhthi.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;

//    @Autowired
//    public ChatController(ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
//        this.chatMessageService = chatMessageService;
//        this.chatRoomService = chatRoomService;
//    }

    @GetMapping("/chat/{roomId}")
    public ResponseEntity<?> getChatMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatMessageService.getChatMessages(roomId));
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatMessage.getRoomId());
        simpMessagingTemplate.convertAndSendToUser(chatRoom.getUserId2().toString(),"/queue/messages" , savedMsg );
    }

//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor) {
//        // Add username in web socket session
//        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSenderId());
//        return chatMessage;
//    }

}
