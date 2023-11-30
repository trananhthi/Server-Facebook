package com.example.trananhthi.repository;

import com.example.trananhthi.dto.CommentDTO;
import com.example.trananhthi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {
    @Query("SELECT new com.example.trananhthi.dto.CommentDTO(c.id, c.userPost.id,c.userAccount,c.content, c.createdAt,c.updatedAt) " +
            "FROM Comment c WHERE c.userPost.id = :postId and c.status = :status")
    Page<CommentDTO> findCommentsByPostId(@Param("postId") Long postId, @Param("status") String status, Pageable pageable);

    @Query("SELECT new com.example.trananhthi.dto.CommentDTO(c.id, c.userPost.id,c.userAccount,c.content, c.createdAt,c.updatedAt) FROM Comment c WHERE c.userPost.id = :userPostId and c.status = :status ORDER BY c.createdAt DESC")
    List<CommentDTO> findTop2CommentsByCreatedAt(@Param("userPostId") Long userPostId,@Param("status") String status);

    Long countAllByUserPost_IdAndStatus(Long postId, String status);
}
