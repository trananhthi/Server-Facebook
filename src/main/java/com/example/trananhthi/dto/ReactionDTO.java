package com.example.trananhthi.dto;

import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.entity.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(value = {"status"})
public class ReactionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String postId;
    private UserAccountDTO userAccount;
    private String typeReaction;
    private Date createdAt;

    public  ReactionDTO(String id, String postId, UserAccount userAccount, String typeReaction, Date createdAt)
    {
        MapEntityToDTO mapEntityToDTO = MapEntityToDTO.getInstance();
        this.id = id;
        this.postId = postId;
        this.userAccount = mapEntityToDTO.mapUserAccountToDTO(userAccount);
        this.typeReaction = typeReaction;
        this.createdAt = createdAt;

    }
}
