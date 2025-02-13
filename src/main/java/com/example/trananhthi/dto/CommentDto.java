package com.example.trananhthi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(value = {"status"})
public class CommentDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String postId;
    private UserAccountDto userAccount;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto(String id, String postId, UserAccountDto userAccount, String content, LocalDateTime createdAt, LocalDateTime updatedAt)
    {
        this.id = id;
        this.postId = postId;
        this.userAccount = userAccount;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
