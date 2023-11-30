package com.example.trananhthi.service;

import com.example.trananhthi.dto.CommentDTO;
import com.example.trananhthi.entity.Comment;
import com.example.trananhthi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment)
    {
        return commentRepository.save(comment);
    }

    @Transactional
    public Page<CommentDTO> getAllCommentByUserPostID(Long userPostID, String status, Pageable pageable)
    {
        return commentRepository.findCommentsByPostId(userPostID,status,pageable);
    }

    public List<CommentDTO> getTop2LatestComments(Long userPostID, String status)
    {
        List<CommentDTO> commentList = commentRepository.findTop2CommentsByCreatedAt(userPostID,status);
        if (commentList.size() > 2) {
            return commentList.subList(0, 2);
        } else {
            return commentList;
        }
    }

    public Long totalComments(Long userPostID,String status)
    {
        return commentRepository.countAllByUserPost_IdAndStatus(userPostID,status);
    }
}
