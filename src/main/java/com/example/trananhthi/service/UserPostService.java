package com.example.trananhthi.service;

import com.example.trananhthi.dto.CreatePostDTO;
import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.repository.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserPostService {
    private final UserPostRepository userPostRepository;

    @Autowired
    public UserPostService( UserPostRepository userPostRepository) {
        this.userPostRepository = userPostRepository;
    }

    public UserPost createNewPost(UserPost userPost)
    {
        return userPostRepository.save(userPost);
    }

    @Transactional
    public Page<UserPost> getAllPost(Pageable pageable)
    {
        return userPostRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public List<UserPost> getAllUserPostsByAuthorId(Long authorId) {
        return userPostRepository.findAllByAuthor_Id(authorId);
    }

    public UserPost getUserPostByID(Long id){
        Optional<UserPost> userPost = userPostRepository.findById(id);
        if(userPost.isPresent()){
            return userPost.get();
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "PostIsInexist","Bài đăng không tồn tại");
        }
    }

    public UserPost updateUserPostByID(Long id,String email, CreatePostDTO dto)
    {
        UserPost userPost = getUserPostByID(id);
        if(userPost.getAuthor().getEmail().equals(email))
        {
            userPost.setContent(dto.getContent());
            userPost.setTypePost(dto.getTypePost());
            userPost.setPrivacy(dto.getPrivacy());
            return userPostRepository.save(userPost);
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),"YouAreNotAllowed","Bạn không được phép chỉnh sửa bài viết này");
        }

    }
}
