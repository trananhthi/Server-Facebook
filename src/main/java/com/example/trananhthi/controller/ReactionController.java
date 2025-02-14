package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.dto.ReactionDto;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.service.ReactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReactionController extends BaseController {
    private final ReactionService reactionService;
    private static final String ROOT = "/reaction";

    @PostMapping(V1 + ROOT + "/express/{postId}")
    public ResponseEntity<?> expressReaction (@PathVariable String postId, @RequestBody ReactionDto dto,
                                              HttpServletRequest request)
    {
        if(dto.getStatus() == null) dto.setStatus(Status.ACT);

        return ResponseEntity.status(HttpStatus.CREATED).body(reactionService.expressReaction(postId, dto.getTypeReaction(), dto.getStatus().toString(), request));
    }

    @GetMapping(V1 + ROOT + "/get/{postId}")
    public ResponseEntity<List<ReactionDto>> ggetAllReactionByUserPostID(@PathVariable String postId)
    {
        List<ReactionDto> reactionList=  reactionService.getAllReactionByUserPostID(postId);
        return ResponseEntity.ok().body(reactionList);
    }
}
