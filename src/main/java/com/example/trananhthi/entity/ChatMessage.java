package com.example.trananhthi.entity;

import com.example.trananhthi.enumtype.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "chat_message")
@Getter
@Setter
public class ChatMessage {
    @Id
    private String id;

    @Field("room_id")
    private String roomId;

    @Field("sender_id")
    private String senderId;

    @Field("content")
    private String content;

    @Field("status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Field("created_at")
    private LocalDateTime createdAt;

    public ChatMessage() {
        this.status = Status.ACT;
        this.createdAt = LocalDateTime.now();
    }
}