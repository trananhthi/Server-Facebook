package com.example.trananhthi.dto;

import com.example.trananhthi.entity.UserPost;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(value = {"priority","status"})
public class UserPostDTO {
    private Long id;
    private UserAccountDTO author;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private String typePost;
    private List<PostImageDTO> image;
    private UserPost parentPost;
    private Integer view;
    private String privacy;
    private String tag;
    private String hashtag;
}
