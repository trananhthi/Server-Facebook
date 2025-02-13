package com.example.trananhthi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto {
    private String id;

    private String roomId;

    private UserAccountDto sender;

    private String content;

    private String status;

    private LocalDateTime createdAt;
}
