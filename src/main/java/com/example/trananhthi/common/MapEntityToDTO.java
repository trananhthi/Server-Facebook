package com.example.trananhthi.common;

import com.example.trananhthi.dto.ChatRoomDto;
import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.dto.UserAccountDto;
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

    public UserAccountDto mapUserAccountToDTO(UserAccount userAccount)
    {
        return objectMapper.convertValue(userAccount, UserAccountDto.class);
    }

    public UserPostDTO mapUserPostToDTO(UserPost userPost)
    {
        return objectMapper.convertValue(userPost,UserPostDTO.class);
    }

    public UserPost mapCreatePostDTOToEntity(CreatePostDto createPostDTO)
    {
        return objectMapper.convertValue(createPostDTO,UserPost.class);
    }

    public List<UserAccountDto> mapUserAccountListToDTOList(List<UserAccount> userAccountList) {
        JavaType targetType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserAccountDto.class);
        return objectMapper.convertValue(userAccountList, targetType);
    }

    public ChatRoomDto mapChatRoomToDTO(ChatRoom chatRoom)
    {
        return objectMapper.convertValue(chatRoom, ChatRoomDto.class);
    }
}
