package com.example.trananhthi.service;

import com.example.trananhthi.dto.RestPage;
import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.dto.UserPostDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserPostService {
    UserPostDto createNewPost(CreatePostDto dto, List<MultipartFile> imageFiles,
                              List<MultipartFile> videoFiles, HttpServletRequest request);

    RestPage<UserPostDto> getAllPost(Pageable pageable);

    UserPostDto updateUserPostById(String id, CreatePostDto dto, List<MultipartFile> imageFiles,
                                   List<MultipartFile> videoFiles, HttpServletRequest request);
}
