package com.example.trananhthi.repository;

import com.example.trananhthi.dto.CommentDto;
import com.example.trananhthi.entity.Comment;
import com.example.trananhthi.enumtype.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment,String> {
    @Query("SELECT new com.example.trananhthi.dto.CommentDto(c.id, c.postId, " +
            "new com.example.trananhthi.dto.UserAccountDto(u.id, u.email, u.firstName, u.lastName, " +
            "u.phone, u.birthday, u.gender, u.avatar, u.createdAt, u.privacyDefault), " +
            "c.content, c.createdAt,c.updatedAt) " +
            "FROM Comment c " +
            "JOIN UserAccount u ON c.userId = u.id " +
            "WHERE c.postId = :postId " +
            "AND c.status = :status " +
            "ORDER BY c.createdAt DESC")
    Page<CommentDto> findCommentsByPostId(@Param("postId") String postId, @Param("status") String status, Pageable pageable);

    Long countAllByPostIdAndStatus(String postId, Status status);
}
