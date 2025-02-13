package com.example.trananhthi.repository;

import com.example.trananhthi.dto.ReactionDto;
import com.example.trananhthi.entity.Reaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends CrudRepository<Reaction,String> {

    @Query("SELECT new com.example.trananhthi.dto.ReactionDto(" +
            "r.id, r.postId, " +
            "new com.example.trananhthi.dto.UserAccountDto(u.id, u.email, u.firstName, u.lastName, " +
            "u.phone, u.birthday, u.gender, u.avatar, u.createdAt, u.privacyDefault), " +
            "r.typeReaction, r.createdAt) " +
            "FROM Reaction r " +
            "JOIN UserAccount u ON r.userId = u.id " +
            "WHERE r.postId = :postId AND r.status = 'ACT'")
    List<ReactionDto> findReactionsByPostId(@Param("postId") String postId);

    @Query("SELECT r FROM Reaction r WHERE r.postId = :postId and r.userId = :userId")
    Optional<Reaction> findByPostIdAndUserId(String postId, String userId);

}
