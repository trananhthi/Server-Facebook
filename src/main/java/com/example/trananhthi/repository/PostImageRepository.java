package com.example.trananhthi.repository;

import com.example.trananhthi.entity.PostMedia;
import com.example.trananhthi.enumtype.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends CrudRepository<PostMedia,String> {
    @Query("SELECT p " +
            "FROM PostMedia p WHERE p.postId = :postId " +
            "AND p.status = :status")
    List<PostMedia> findAllMediaByPostId(String postId, Status status);

    Optional<PostMedia> findByIdAndStatus(String id, Status status);
}
