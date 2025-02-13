package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController extends BaseController {
    private final ChatMessageService chatMessageService;
    private static final String ROOT = "/chat-message";

    @GetMapping(V1 + ROOT + "/{roomId}")
    public ResponseEntity<?> getChatMessages(@PathVariable String roomId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30")  int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(chatMessageService.getChatMessages(roomId,pageable));
    }

    @MessageMapping(V1 + ROOT)
    public void processMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.processMessage(chatMessage);
    }
}
