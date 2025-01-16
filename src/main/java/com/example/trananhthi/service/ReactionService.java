package com.example.trananhthi.service;

import com.example.trananhthi.dto.ReactionDTO;

import java.util.List;

public interface ReactionService {
    int expressReaction(String postId,String userId,String typeReaction,String status);

    List<ReactionDTO> getAllReactionByUserPostID(String userPostID, String status);

}
