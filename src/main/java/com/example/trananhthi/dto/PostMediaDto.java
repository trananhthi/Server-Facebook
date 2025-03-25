package com.example.trananhthi.dto;

import com.example.trananhthi.enumtype.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String url;

    private String status;

    private MediaType type;

    private Integer size;

    private Integer visualIndex;

    private Integer width;

    private Integer height;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
