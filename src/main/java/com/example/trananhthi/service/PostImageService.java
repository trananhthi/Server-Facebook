package com.example.trananhthi.service;

import com.example.trananhthi.dto.PostImageDTO;
import com.example.trananhthi.entity.PostImage;

import java.util.List;

public interface PostImageService {
    void createImage(PostImage postImage);

    List<PostImageDTO> getAllImageByPostId(String userPostID, String status);

    Boolean deleteImage(String id);
}
