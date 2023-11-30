package com.example.trananhthi.service;

import com.example.trananhthi.dto.ReactionDTO;
import com.example.trananhthi.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public int expressReaction(Long postId,Long userId,String typeReaction,String status)
    {
        if(status == null) status = "active";
        return reactionRepository.insertOrUpdateReaction(postId,userId,typeReaction,status);
    }

    public List<ReactionDTO> getAllReactionByUserPostID(Long userPostID,String status)
    {
        return reactionRepository.findReactionsByPostId(userPostID,status);
    }

}
