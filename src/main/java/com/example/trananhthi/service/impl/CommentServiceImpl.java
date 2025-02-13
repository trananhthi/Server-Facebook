package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.dto.CommentDto;
import com.example.trananhthi.entity.Comment;
import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.repository.CommentRepository;
import com.example.trananhthi.repository.UserPostRepository;
import com.example.trananhthi.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends BaseServiceImpl<Comment, CommentRepository> implements CommentService {
    private final CommentRepository commentRepository;
    private final UserPostRepository userPostRepository;

    @Override
    public Comment createComment(String postId, CommentDto dto, HttpServletRequest request)
    {
        Optional<UserPost> userPost = userPostRepository.findById(postId);
        if (userPost.isEmpty())
        {
            String message = getMessageCode(MessageCodes.POST_NOTFOUND, request, postId);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.POST_NOTFOUND, message);
        }

        String userId = UserContext.getUserId();
        Comment newComment = new Comment();
        newComment.setPostId(postId);
        newComment.setUserId(userId);
        newComment.setContent(dto.getContent());
        newComment.setStatus(Status.ACT);
        try{
            return commentRepository.save(newComment);
        }
        catch (Exception e)
        {
            String message = getMessageCode(MessageCodes.SOMETHING_WRONG, request);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.SOMETHING_WRONG, message);
        }
    }

    @Override
    public Page<CommentDto> getAllCommentByUserPostID(String postId, String status, Pageable pageable)
    {
        return commentRepository.findCommentsByPostId(postId,status,pageable);
    }

    @Override
    public List<CommentDto> getTop2LatestComments(String postId, String status)
    {
        Pageable pageable = PageRequest.of(0, 2);
        return commentRepository.findCommentsByPostId(postId,status,pageable).getContent();
    }

    @Override
    public Long totalComments(String postId,String status)
    {
        return commentRepository.countAllByPostIdAndStatus(postId,Enum.valueOf(Status.class, status));
    }
}
