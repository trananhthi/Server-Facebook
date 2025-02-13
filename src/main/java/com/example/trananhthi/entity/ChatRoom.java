package com.example.trananhthi.entity;

import com.example.trananhthi.common.BaseEntity;
import com.example.trananhthi.enumtype.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRoom extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "user1_id")
    private String userId1;

    @Column(name = "user2_id")
    private String userId2;

    @Column(name = "room_name")
    private String roomName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "last_message_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastMessageTime;
}
