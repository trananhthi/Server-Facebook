package com.example.trananhthi.service;

import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.dto.RestPage;
import com.example.trananhthi.dto.UserPostDTO;
import com.example.trananhthi.entity.UserPost;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserPostService {
    UserPost createNewPost(UserPost userPost);

    RestPage<UserPostDTO> getAllPost(Pageable pageable);

    List<UserPost> getAllUserPostsByAuthorId(String authorId);

    UserPost getUserPostByID(String id);

    UserPost updateUserPostByID(String id,String email, CreatePostDto dto);
}
