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
public class ReactionDTO {
    private Long id;
    private Long postId;
    private UserAccountDTO userAccount;
    private String typeReaction;
    private Date createdAt;

    public  ReactionDTO(Long id, Long postId, UserAccount userAccount, String typeReaction, Date createdAt)
    {
        this.id = id;
        this.postId = postId;
        this.userAccount = MapEntityToDTO.mapUserAccountToDTO(userAccount);
        this.typeReaction = typeReaction;
        this.createdAt = createdAt;

    }
}
