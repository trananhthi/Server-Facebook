package com.example.trananhthi.dto;

import com.example.trananhthi.enumtype.Status;
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

    public PostImageDto(String id, String url, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.url = url;
        this.status = status.getValue();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
