package com.example.trananhthi.service;

import com.example.trananhthi.dto.CommentDto;
import com.example.trananhthi.entity.Comment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    Comment createComment(String postId, CommentDto dto, HttpServletRequest request);

    Page<CommentDto> getAllCommentByUserPostID(String postId, String status, Pageable pageable);

    List<CommentDto> getTop2LatestComments(String postId, String status);

    Long totalComments(String postId,String status);
}
