package com.example.trananhthi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TypingStatus {
    private String chatRoomId;

    private String userId;

    @JsonProperty("isTyping")
    private boolean isTyping;
}
