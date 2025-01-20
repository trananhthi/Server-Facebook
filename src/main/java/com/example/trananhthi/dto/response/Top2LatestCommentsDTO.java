package com.example.trananhthi.dto.response;

import com.example.trananhthi.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Top2LatestCommentsDTO {
    private List<CommentDto> commentList;
    private Long total;
}
