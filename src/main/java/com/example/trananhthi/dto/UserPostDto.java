package com.example.trananhthi.dto;

import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.enumtype.Privacy;
import com.example.trananhthi.enumtype.TypePost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserPostDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private UserAccountDto author;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private TypePost typePost;

    private List<PostMediaDto> mediaList;

    private UserPost parentPost;

    private Integer view;

    private Privacy privacy;

    private String tag;

    private String hashtag;
}
