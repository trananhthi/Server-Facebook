package com.example.trananhthi.service;

import com.example.trananhthi.dto.CommentDTO;
import com.example.trananhthi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    Comment createComment(Comment comment);

    Page<CommentDTO> getAllCommentByUserPostID(String userPostID, String status, Pageable pageable);

    List<CommentDTO> getTop2LatestComments(String userPostID, String status);

    Long totalComments(String userPostID,String status);
}
