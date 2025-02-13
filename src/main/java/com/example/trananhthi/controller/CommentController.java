package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.dto.CommentDto;
import com.example.trananhthi.dto.response.Top2LatestCommentsDTO;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController extends BaseController {
    private final CommentService commentService;

    private static final String ROOT = "/comment";

    @PostMapping(V1 + ROOT + "/create/{postId}")
    public ResponseEntity<?> createComment(@PathVariable String postId, @RequestBody CommentDto dto,
                                           HttpServletRequest request)
    {
        return new ResponseEntity<>(commentService.createComment(postId, dto, request), HttpStatus.CREATED);
    }

    @GetMapping(V1 + ROOT + "/get/{postId}")
    public ResponseEntity<Page<CommentDto>> getAllCommentByUserPostID(@PathVariable String postId, Pageable pageable)
    {
        Page<CommentDto> commentList=  commentService.getAllCommentByUserPostID(postId,Status.ACT.toString(),pageable);
        return ResponseEntity.ok().body(commentList);
    }

    @GetMapping(V1 + ROOT + "/get/top-2-lastest-comments/{postId}")
    public ResponseEntity<Top2LatestCommentsDTO> getTop2LatestCommentsDTO(@PathVariable String postId)
    {
        List<CommentDto> commentList=  commentService.getTop2LatestComments(postId,Status.ACT.toString());
        Long total = commentService.totalComments(postId, Status.ACT.toString());
        return ResponseEntity.ok().body(new Top2LatestCommentsDTO(commentList,total));
    }

}
