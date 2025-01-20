package com.example.trananhthi.service;

import com.example.trananhthi.dto.ReactionDto;

import java.util.List;

public interface ReactionService {
    int expressReaction(String postId,String userId,String typeReaction,String status);

    List<ReactionDto> getAllReactionByUserPostID(String userPostID, String status);

}
