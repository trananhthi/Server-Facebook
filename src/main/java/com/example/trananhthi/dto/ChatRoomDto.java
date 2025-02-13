package com.example.trananhthi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String userId1;

    private String userId2;

    private String roomName;

    private String status;

    private LocalDateTime createdAt;

    private UserAccountDto receiver = null;

    private LocalDateTime lastMessageTime;

    private ChatMessageDto lastMessage;
}
