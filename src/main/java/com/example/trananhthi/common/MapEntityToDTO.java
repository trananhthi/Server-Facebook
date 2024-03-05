package com.example.trananhthi.common;

import com.example.trananhthi.dto.ChatRoomDTO;
import com.example.trananhthi.dto.CreatePostDTO;
import com.example.trananhthi.dto.UserAccountDTO;
import com.example.trananhthi.dto.UserPostDTO;
import com.example.trananhthi.entity.ChatRoom;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.entity.UserPost;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class MapEntityToDTO {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MapEntityToDTO instance = new MapEntityToDTO();

    private MapEntityToDTO() {
        // private constructor thực thi signleton pattern
    }

    public static MapEntityToDTO getInstance() {
        return instance;
    }

    public UserAccountDTO mapUserAccountToDTO(UserAccount userAccount)
    {
        return objectMapper.convertValue(userAccount,UserAccountDTO.class);
    }

    public UserPostDTO mapUserPostToDTO(UserPost userPost)
    {
        return objectMapper.convertValue(userPost,UserPostDTO.class);
    }

    public UserPost mapCreatePostDTOToEntity(CreatePostDTO createPostDTO)
    {
        return objectMapper.convertValue(createPostDTO,UserPost.class);
    }

    public List<UserAccountDTO> mapUserAccountListToDTOList(List<UserAccount> userAccountList) {
        JavaType targetType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserAccountDTO.class);
        return objectMapper.convertValue(userAccountList, targetType);
    }

    public ChatRoomDTO mapChatRoomToDTO(ChatRoom chatRoom)
    {
        return objectMapper.convertValue(chatRoom,ChatRoomDTO.class);
    }
}
