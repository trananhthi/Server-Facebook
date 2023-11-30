package com.example.trananhthi.common;

import com.example.trananhthi.dto.CreatePostDTO;
import com.example.trananhthi.dto.ReactionDTO;
import com.example.trananhthi.dto.UserAccountDTO;
import com.example.trananhthi.dto.UserPostDTO;
import com.example.trananhthi.entity.Reaction;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.entity.UserPost;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapEntityToDTO {
    public static UserAccountDTO mapUserAccountToDTO(UserAccount userAccount)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(userAccount,UserAccountDTO.class);

    }

    public static UserPostDTO mapUserPostToDTO(UserPost userPost)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(userPost,UserPostDTO.class);
    }

    public static UserPost mapCreatePostDTOToEntity(CreatePostDTO createPostDTO)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(createPostDTO,UserPost.class);
    }

    public static ReactionDTO mapReactionToDTO(Reaction reaction)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(reaction,ReactionDTO.class);
    }
}
