package com.example.trananhthi.repository;

import com.example.trananhthi.dto.PostImageDto;
import com.example.trananhthi.entity.PostImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends CrudRepository<PostImage,String> {
    @Query("SELECT new com.example.trananhthi.dto.PostImageDto(p.id, p.url) " +
            "FROM PostImage p WHERE p.userPost.id = :postId and p.status = :status")
    List<PostImageDto> findAllImageByPostId(@Param("postId") String postId, @Param("status") String status);

    Optional<PostImage> findByIdAndStatus(String id,String status);
}
