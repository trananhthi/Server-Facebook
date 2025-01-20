package com.example.trananhthi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(value = {"status","createdAt"})
@NoArgsConstructor
public class PostImageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String url;

    public PostImageDto(String id, String url) {
        this.id = id;
        this.url = url;
    }

}
