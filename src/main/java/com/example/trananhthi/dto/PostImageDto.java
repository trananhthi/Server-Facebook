package com.example.trananhthi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostImageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String url;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostImageDto(String id, String url) {
        this.id = id;
        this.url = url;
    }

}
