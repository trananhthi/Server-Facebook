package com.example.trananhthi.dto;

import com.example.trananhthi.entity.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ChatRoomDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId1;
    private String userId2;
    private String roomName;
    private String status;
    private Date createdAt;
    private UserAccountDTO receiver = null;
    private Date lastMessageTime;
    private ChatMessage lastMessage;
}
