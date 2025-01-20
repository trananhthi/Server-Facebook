package com.example.trananhthi.service;

import com.example.trananhthi.dto.CommentDto;
import com.example.trananhthi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    Comment createComment(Comment comment);

    Page<CommentDto> getAllCommentByUserPostID(String userPostID, String status, Pageable pageable);

    List<CommentDto> getTop2LatestComments(String userPostID, String status);

    Long totalComments(String userPostID,String status);
}
