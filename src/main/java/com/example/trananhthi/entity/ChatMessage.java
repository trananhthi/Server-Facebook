package com.example.trananhthi.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "chat_message")
@Data
public class ChatMessage {
    @Id
    private String id;
    @Field("room_id")
    private Long roomId;
    @Field("sender_id")
    private Long senderId;
    @Field("content")
    private String content;
    @Field("status")
    private String status;
    @Field("created_at")
    private Date createdAt;

    public ChatMessage() {
        this.status = "active";
        this.createdAt = new Date();
    }
}