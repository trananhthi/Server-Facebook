package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.dto.response.CustomSuccessResponse;
import com.example.trananhthi.dto.request.ExpressReactionDto;
import com.example.trananhthi.dto.ReactionDto;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.service.JwtService;
import com.example.trananhthi.service.ReactionService;
import com.example.trananhthi.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReactionController extends BaseController {
    private final ReactionService reactionService;
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private static final String ROOT = "/reaction";

    @PostMapping(V1 + ROOT + "/express/{postID}")
    public ResponseEntity<?> expressReaction (@RequestHeader(name = "Authorization") String token,@PathVariable String postID, @RequestBody ExpressReactionDto dto)
    {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String email = jwtService.extractUsername(jwtToken);
            String userId = userAccountService.getUserByEmail(email).get().getId();
            if (reactionService.expressReaction(postID,userId,dto.getTypeReaction(),dto.getStatus()) > 0)
            {
                return ResponseEntity.status(HttpStatus.CREATED).body(new CustomSuccessResponse("Bày tỏ cảm xúc thành công","success"));
            }
            else{
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "ReactionIsNotExpressed","Không thể bày tỏ cảm xúc");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping(V1 + ROOT + "/get/{postID}")
    public ResponseEntity<List<ReactionDto>> ggetAllReactionByUserPostID(@PathVariable String postID)
    {
        List<ReactionDto> reactionList=  reactionService.getAllReactionByUserPostID(postID,"active");
        return ResponseEntity.ok().body(reactionList);
    }
}
