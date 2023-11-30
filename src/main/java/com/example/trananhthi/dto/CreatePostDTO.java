package com.example.trananhthi.dto;

import com.example.trananhthi.entity.UserPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostDTO {
    private String content;
    private String typePost;
    private String video;
    private UserPost parentPost;
    private String privacy;
    private String tag;
    private String hashtag;
}
