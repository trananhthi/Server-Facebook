package com.example.trananhthi.repository;

import com.example.trananhthi.entity.UserPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPostRepository extends CrudRepository<UserPost,String> {
    List<UserPost> findAllByAuthor_Id(String authorId);
    Page<UserPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
