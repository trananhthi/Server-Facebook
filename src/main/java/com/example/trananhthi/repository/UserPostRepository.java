package com.example.trananhthi.repository;

import com.example.trananhthi.entity.UserPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPostRepository extends CrudRepository<UserPost,Long> {
    List<UserPost> findAllByAuthor_Id(Long authorId);
    Optional<UserPost> findById(Long id);
    Page<UserPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
