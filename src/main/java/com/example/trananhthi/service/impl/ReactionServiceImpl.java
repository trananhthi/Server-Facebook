package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.dto.ReactionDto;
import com.example.trananhthi.entity.Reaction;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.mapstruct.ReactionMapper;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.repository.ReactionRepository;
import com.example.trananhthi.service.ReactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl extends BaseServiceImpl<Reaction, ReactionRepository> implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;

    @Override
    public ReactionDto expressReaction(String postId, String typeReaction, String status, HttpServletRequest request)
    {
        try
        {
            String userId = UserContext.getUserId();
            Optional<Reaction> reaction = reactionRepository.findByPostIdAndUserId(postId,userId);
            if(reaction.isPresent())
            {
                reaction.get().setTypeReaction(typeReaction);
                reaction.get().setStatus(Enum.valueOf(Status.class, status));
                return reactionMapper.toDto(reactionRepository.save(reaction.get()));
            }
            else
            {
                Reaction newReaction = new Reaction();
                newReaction.setPostId(postId);
                newReaction.setUserId(userId);
                newReaction.setTypeReaction(typeReaction);
                newReaction.setStatus(Enum.valueOf(Status.class, status));
                return reactionMapper.toDto(reactionRepository.save(newReaction));
            }
        }
        catch (Exception e)
        {
            String message = getMessageCode(MessageCodes.SOMETHING_WRONG, request);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.SOMETHING_WRONG, message);
        }

    }

    @Override
    public List<ReactionDto> getAllReactionByUserPostID(String userPostId)
    {
        return reactionRepository.findReactionsByPostId(userPostId);
    }
}
