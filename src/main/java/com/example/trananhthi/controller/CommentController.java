package com.example.trananhthi.controller;

import com.example.trananhthi.common.CustomSuccessResponse;
import com.example.trananhthi.dto.CommentDTO;
import com.example.trananhthi.dto.CreateCommentDTO;
import com.example.trananhthi.dto.Top2LatestCommentsDTO;
import com.example.trananhthi.entity.Comment;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.service.CommentService;
import com.example.trananhthi.service.JwtService;
import com.example.trananhthi.service.UserAccountService;
import com.example.trananhthi.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private final UserPostService userPostService;

    @Autowired
    public CommentController(CommentService commentService, UserAccountService userAccountService, JwtService jwtService, UserPostService userPostService) {
        this.commentService = commentService;
        this.userAccountService = userAccountService;
        this.jwtService = jwtService;
        this.userPostService = userPostService;
    }

    @PostMapping("/create/{postID}")
    public ResponseEntity<?> createComment(@RequestHeader(name = "Authorization") String token, @PathVariable Long postID, @RequestBody CreateCommentDTO dto)
    {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String email = jwtService.extractUsername(jwtToken);
            Comment newComment = new Comment();
            newComment.setUserPost(userPostService.getUserPostByID(postID));
            newComment.setUserAccount(userAccountService.getUserByEmail(email).get());
            newComment.setContent(dto.getContent());
            if (commentService.createComment(newComment).getId() > 0)
            {
                return ResponseEntity.status(HttpStatus.CREATED).body(new CustomSuccessResponse("Đăng bình luận thành công","success"));
            }
            else{
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "CommentDoesNotPost","Không thể bình luận bài viết này");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/get/{postID}")
    public ResponseEntity<Page<CommentDTO>> getAllCommentByUserPostID(@PathVariable Long postID,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "7")  int size)
    {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<CommentDTO> commentList=  commentService.getAllCommentByUserPostID(postID,"active",pageable);
        return ResponseEntity.ok().body(commentList);
    }

    @GetMapping("/get/top-2-lastest-comments/{postID}")
    public ResponseEntity<Top2LatestCommentsDTO> getTop2LatestCommentsDTO(@PathVariable Long postID)
    {
        List<CommentDTO> commentList=  commentService.getTop2LatestComments(postID,"active");
        Long total = commentService.totalComments(postID,"active");
        return ResponseEntity.ok().body(new Top2LatestCommentsDTO(commentList,total));
    }

}
