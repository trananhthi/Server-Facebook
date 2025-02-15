package com.example.trananhthi.service;

import com.example.trananhthi.dto.PostMediaDto;

import java.util.List;

public interface PostMediaService {

    List<PostMediaDto> findAllMediaByPostId(String userPostId, String status);

    Boolean deleteMedia(String id);
}
