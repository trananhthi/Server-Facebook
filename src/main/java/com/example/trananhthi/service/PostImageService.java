package com.example.trananhthi.service;

import com.example.trananhthi.dto.PostImageDto;
import com.example.trananhthi.entity.PostImage;

import java.util.List;

public interface PostImageService {
    void createImage(PostImage postImage);

    List<PostImageDto> getAllImageByPostId(String userPostID, String status);

    Boolean deleteImage(String id);
}
