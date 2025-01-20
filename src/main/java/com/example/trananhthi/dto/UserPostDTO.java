package com.example.trananhthi.dto;

import com.example.trananhthi.entity.UserPost;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(value = {"priority","status"})
public class UserPostDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private UserAccountDto author;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private String typePost;
    private List<PostImageDto> image;
    private UserPost parentPost;
    private Integer view;
    private String privacy;
    private String tag;
    private String hashtag;
}
