package com.example.trananhthi.dto;

import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.entity.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(value = {"status"})
public class CommentDTO {
    private Long id;
    private Long postId;
    private UserAccountDTO userAccount;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    public  CommentDTO(Long id, Long postId, UserAccount userAccount, String content, Date createdAt, Date updatedAt)
    {
        this.id = id;
        this.postId = postId;
        this.userAccount = MapEntityToDTO.mapUserAccountToDTO(userAccount);
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
