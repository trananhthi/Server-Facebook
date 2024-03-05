package com.example.trananhthi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatRoomDTO {
    private Long id;
    private Long userId1;
    private Long userId2;
    private String roomName;
    private String status;
    private Date createdAt;
    private UserAccountDTO receiver = null;
}
