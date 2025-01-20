package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.dto.ReactionDto;
import com.example.trananhthi.entity.Reaction;
import com.example.trananhthi.repository.ReactionRepository;
import com.example.trananhthi.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl extends BaseServiceImpl<Reaction, ReactionRepository> implements ReactionService {
    private final ReactionRepository reactionRepository;

    @Override
    public int expressReaction(String postId,String userId,String typeReaction,String status)
    {
        if(status == null) status = "active";
        return reactionRepository.insertOrUpdateReaction(postId,userId,typeReaction,status);
    }

    @Override
    public List<ReactionDto> getAllReactionByUserPostID(String userPostID, String status)
    {
        return reactionRepository.findReactionsByPostId(userPostID,status);
    }
}
