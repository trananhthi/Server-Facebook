package com.example.trananhthi.service;

import com.example.trananhthi.dto.ReactionDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ReactionService {
    ReactionDto expressReaction(String postId, String typeReaction, String status, HttpServletRequest request);

    List<ReactionDto> getAllReactionByUserPostID(String userPostId);

}
