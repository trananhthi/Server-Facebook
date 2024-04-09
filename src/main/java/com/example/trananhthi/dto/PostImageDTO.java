package com.example.trananhthi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(value = {"status","createdAt"})
@NoArgsConstructor
public class PostImageDTO {
    private Long id;
    private String url;

    public PostImageDTO(Long id, String url) {
        this.id = id;
        this.url = url;
    }

}
