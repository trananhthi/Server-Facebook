package com.example.trananhthi.model.request;

import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.enumtype.Privacy;
import com.example.trananhthi.enumtype.TypePost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostDto {
    private String content;
    private TypePost typePost;
    private UserPost parentPost;
    private Privacy privacy;
    private String tag;
    private String hashtag;
}
