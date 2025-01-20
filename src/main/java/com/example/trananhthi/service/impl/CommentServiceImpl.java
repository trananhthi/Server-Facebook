package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.dto.CommentDto;
import com.example.trananhthi.entity.Comment;
import com.example.trananhthi.repository.CommentRepository;
import com.example.trananhthi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends BaseServiceImpl<Comment, CommentRepository> implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(Comment comment)
    {
        return commentRepository.save(comment);
    }

    @Override
    public Page<CommentDto> getAllCommentByUserPostID(String userPostID, String status, Pageable pageable)
    {
        return commentRepository.findCommentsByPostId(userPostID,status,pageable);
    }

    @Override
    public List<CommentDto> getTop2LatestComments(String userPostID, String status)
    {
        List<CommentDto> commentList = commentRepository.findTop2CommentsByCreatedAt(userPostID,status);
        if (commentList.size() > 2) {
            return commentList.subList(0, 2);
        } else {
            return commentList;
        }
    }

    @Override
    public Long totalComments(String userPostID,String status)
    {
        return commentRepository.countAllByUserPost_IdAndStatus(userPostID,status);
    }
}
