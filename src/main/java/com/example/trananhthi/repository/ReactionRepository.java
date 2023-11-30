package com.example.trananhthi.repository;

import com.example.trananhthi.dto.ReactionDTO;
import com.example.trananhthi.entity.Reaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReactionRepository extends CrudRepository<Reaction,Long> {
    @Query("SELECT new com.example.trananhthi.dto.ReactionDTO(r.id, r.userPost.id,r.userAccount,r.typeReaction, r.createdAt) " +
            "FROM Reaction r WHERE r.userPost.id = :postId and r.status = :status")
    List<ReactionDTO> findReactionsByPostId(@Param("postId") Long postId,@Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reactions (post_id, user_id, type_reaction, status) VALUES (:postId, :userId, :typeReaction, :status) ON DUPLICATE KEY UPDATE post_id = :postId, user_id = :userId, type_reaction = :typeReaction, status = :status", nativeQuery = true)
    int insertOrUpdateReaction(
            @Param("postId") Long postId,
            @Param("userId") Long userId,
            @Param("typeReaction") String typeReaction,
            @Param("status") String status
    );

    //List<Reaction> findAllByUserPost_IdAndStatus(Long id,String status);

}
