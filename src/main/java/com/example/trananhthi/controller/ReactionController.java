package com.example.trananhthi.controller;

import com.example.trananhthi.common.CustomSuccessResponse;
import com.example.trananhthi.dto.ExpressReactionDTO;
import com.example.trananhthi.dto.ReactionDTO;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.service.JwtService;
import com.example.trananhthi.service.ReactionService;
import com.example.trananhthi.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reaction")
public class ReactionController {
    private final ReactionService reactionService;
    private final UserAccountService userAccountService;
    private final JwtService jwtService;

    @Autowired
    public ReactionController(ReactionService reactionService, UserAccountService userAccountService, JwtService jwtService) {
        this.reactionService = reactionService;
        this.userAccountService = userAccountService;
        this.jwtService = jwtService;
    }

    @PostMapping("/express/{postID}")
    public ResponseEntity<?> expressReaction (@RequestHeader(name = "Authorization") String token,@PathVariable Long postID, @RequestBody ExpressReactionDTO dto)
    {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String email = jwtService.extractUsername(jwtToken);
            Long userId = userAccountService.getUserByEmail(email).get(0).getId();
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

    @GetMapping("/get/{postID}")
    public ResponseEntity<List<ReactionDTO>> ggetAllReactionByUserPostID(@PathVariable Long postID)
    {
        List<ReactionDTO> reactionList=  reactionService.getAllReactionByUserPostID(postID,"active");
        return ResponseEntity.ok().body(reactionList);
    }
}
