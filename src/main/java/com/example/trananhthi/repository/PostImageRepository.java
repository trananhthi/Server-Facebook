package com.example.trananhthi.repository;

import com.example.trananhthi.dto.PostImageDto;
import com.example.trananhthi.entity.PostImage;
import com.example.trananhthi.enumtype.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends CrudRepository<PostImage,String> {
    @Query("SELECT new com.example.trananhthi.dto.PostImageDto(p.id, p.url,p.status, p.createdAt, p.updatedAt) " +
            "FROM PostImage p WHERE p.postId = :postId and p.status = :status")
    List<PostImageDto> findAllImageByPostId(String postId, Status status);

    Optional<PostImage> findByIdAndStatus(String id, Status status);
}
