package com.example.trananhthi.service;

import com.example.trananhthi.dto.RestPage;
import com.example.trananhthi.model.request.CreatePostDto;
import com.example.trananhthi.dto.UserPostDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserPostService {
    @Transactional
    UserPostDto createNewPost(CreatePostDto dto, List<MultipartFile> imageFiles, List<Integer> imageIndexes,
                              List<MultipartFile> videoFiles,  List<Integer> videoIndexes, HttpServletRequest request);

    RestPage<UserPostDto> getAllPost(Pageable pageable);

    @Transactional
    UserPostDto updateUserPostById(String id, CreatePostDto dto, List<MultipartFile> imageFiles, List<Integer> imageIndexes,
                                   List<MultipartFile> videoFiles, List<Integer> videoIndexes, HttpServletRequest request);
}
