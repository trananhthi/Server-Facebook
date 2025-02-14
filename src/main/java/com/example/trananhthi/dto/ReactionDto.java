package com.example.trananhthi.dto;

import com.example.trananhthi.enumtype.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String postId;
    private UserAccountDto userAccount;
    private String typeReaction;
    private LocalDateTime createdAt;
    private Status status;

//    public ReactionDto(String id, String postId, UserAccountDto userAccount,
//                       String typeReaction, LocalDateTime createdAt, String status)
//    {
//        this.id = id;
//        this.postId = postId;
//        this.userAccount = userAccount;
//        this.typeReaction = typeReaction;
//        this.createdAt = createdAt;
//    }
}
