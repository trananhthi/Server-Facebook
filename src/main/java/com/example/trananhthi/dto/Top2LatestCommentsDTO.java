package com.example.trananhthi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Top2LatestCommentsDTO {
    private List<CommentDTO> commentList;
    private Long total;
}
