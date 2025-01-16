package com.example.trananhthi.service;

import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.dto.request.CreatePostDTO;
import com.example.trananhthi.dto.RestPage;
import com.example.trananhthi.dto.UserPostDTO;
import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.repository.UserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserPostService {
    UserPost createNewPost(UserPost userPost);

    RestPage<UserPostDTO> getAllPost(Pageable pageable);

    List<UserPost> getAllUserPostsByAuthorId(String authorId);

    UserPost getUserPostByID(String id);

    UserPost updateUserPostByID(String id,String email, CreatePostDTO dto);
}
