package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.dto.RestPage;
import com.example.trananhthi.dto.UserPostDTO;
import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.repository.UserPostRepository;
import com.example.trananhthi.service.PostImageService;
import com.example.trananhthi.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableCaching
public class UserPostServiceImpl extends BaseServiceImpl<UserPost,UserPostRepository> implements UserPostService {
    private final UserPostRepository userPostRepository;
    private final MapEntityToDTO mapEntityToDTO = MapEntityToDTO.getInstance();
    private final PostImageService postImageService;

    @Override
    public UserPost createNewPost(UserPost userPost)
    {
        return userPostRepository.save(userPost);
    }

    @Override
    @Cacheable("allPost")
    public RestPage<UserPostDTO> getAllPost(Pageable pageable)
    {
        Page<UserPost> userPostList = userPostRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<UserPostDTO> userPostDTOList = userPostList.map(userPost -> {
            UserPostDTO userPostDTO = mapEntityToDTO.mapUserPostToDTO(userPost);
            userPostDTO.setImage(postImageService.getAllImageByPostId(userPostDTO.getId(), "actived"));
            return userPostDTO;
        });
        return new RestPage<>(userPostDTOList);
//        return new RestPage<>(userPostRepository.findAllByOrderByCreatedAtDesc(pageable));
    }

    @Override
    public List<UserPost> getAllUserPostsByAuthorId(String authorId) {
        return userPostRepository.findAllByAuthor_Id(authorId);
    }

    @Override
    public UserPost getUserPostByID(String id){
        Optional<UserPost> userPost = userPostRepository.findById(id);
        if(userPost.isPresent()){
            return userPost.get();
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "PostIsInexist","Bài đăng không tồn tại");
        }
    }

    @Override
    public UserPost updateUserPostByID(String id,String email, CreatePostDto dto)
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
