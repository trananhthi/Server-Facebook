package com.example.trananhthi.repository;

import com.example.trananhthi.dto.ReactionDto;
import com.example.trananhthi.entity.Reaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReactionRepository extends CrudRepository<Reaction,String> {
    @Query("SELECT new com.example.trananhthi.dto.ReactionDto(r.id, r.userPost.id,r.userAccount,r.typeReaction, r.createdAt) " +
            "FROM Reaction r WHERE r.userPost.id = :postId and r.status = :status")
    List<ReactionDto> findReactionsByPostId(@Param("postId") String postId, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reactions (id,post_id, user_id, type_reaction, status) VALUES (UUID(),:postId, :userId, :typeReaction, :status) ON DUPLICATE KEY UPDATE post_id = :postId, user_id = :userId, type_reaction = :typeReaction, status = :status", nativeQuery = true)
    int insertOrUpdateReaction(
            @Param("postId") String postId,
            @Param("userId") String userId,
            @Param("typeReaction") String typeReaction,
            @Param("status") String status
    );

    //List<Reaction> findAllByUserPost_IdAndStatus(Long id,String status);

}
