package com.example.trananhthi.repository;

import com.example.trananhthi.dto.PostImageDTO;
import com.example.trananhthi.entity.PostImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends CrudRepository<PostImage,Long> {
    @Query("SELECT new com.example.trananhthi.dto.PostImageDTO(p.id, p.url) " +
            "FROM PostImage p WHERE p.userPost.id = :postId and p.status = :status")
    List<PostImageDTO> findAllImageByPostId(@Param("postId") Long postId, @Param("status") String status);

    Optional<PostImage> findByIdAndStatus(Long id,String status);
}
