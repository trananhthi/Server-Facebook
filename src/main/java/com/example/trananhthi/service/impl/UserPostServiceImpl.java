package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.dto.RestPage;
import com.example.trananhthi.dto.UserPostDto;
import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.entity.PostImage;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.mapstruct.UserPostMapper;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.repository.UserAccountRepository;
import com.example.trananhthi.repository.UserPostRepository;
import com.example.trananhthi.service.PostImageService;
import com.example.trananhthi.service.UserPostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableCaching
public class UserPostServiceImpl extends BaseServiceImpl<UserPost,UserPostRepository> implements UserPostService {
    private final UserPostRepository userPostRepository;
    private final PostImageService postImageService;
    private final UserAccountRepository userAccountRepository;
    private final UserPostMapper userPostMapper;

    @Override
    @SneakyThrows
    public UserPostDto createNewPost(CreatePostDto dto, List<MultipartFile> files, HttpServletRequest request)
    {
        UserPost userPost = new UserPost();
        String userId = UserContext.getUserId();
        UserAccount author = userAccountRepository.findById(userId).orElse(null);
        if(author == null)
        {
            String message = getMessageCode(MessageCodes.SOMETHING_WRONG, request);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.SOMETHING_WRONG, message);
        }
        String userPostId = UUID.randomUUID().toString();
        userPost.setId(userPostId);
        userPost.setContent(dto.getContent());
        userPost.setTypePost(dto.getTypePost());
        userPost.setPrivacy(dto.getPrivacy());
        userPost.setParentPost(dto.getParentPost());
        userPost.setHashtag(dto.getHashtag());
        userPost.setTag(dto.getTag());
        userPost.setAuthor(author);

        userPostRepository.save(userPost);

        if(userPost.getTypePost().equals("image"))
        {
            for (MultipartFile file : files) {
                PostImage postImage = new PostImage();
                postImage.setPostId(userPostId);
                postImage.setUrl(uploadFileToS3("2502-post-image", userId, file));
                postImageService.createImage(postImage);
            }
        }
        return userPostMapper.toDto(userPost);
    }

    @Override
    @Cacheable("allPost")
    public RestPage<UserPostDto> getAllPost(Pageable pageable)
    {
        Page<UserPost> userPostList = userPostRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<UserPostDto> userPostDTOList = userPostList.map(userPost -> {
            UserPostDto userPostDTO = userPostMapper.toDto(userPost);
            userPostDTO.setImage(postImageService.getAllImageByPostId(userPostDTO.getId(), Status.ACT.toString()));
            return userPostDTO;
        });
        return new RestPage<>(userPostDTOList);
    }

    @Override
    public UserPostDto updateUserPostById(String id, CreatePostDto dto, List<MultipartFile> files, HttpServletRequest request)
    {
        String userId = UserContext.getUserId();
        UserPost userPost = userPostRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND.value(), MessageCodes.USERPOST_NOTFOUND ,
                        getMessageCode(MessageCodes.USERPOST_NOTFOUND, request, id)));
        if(userPost.getAuthor().getId().equals(userId))
        {
            userPost.setContent(dto.getContent());
            userPost.setTypePost(dto.getTypePost());
            userPost.setPrivacy(dto.getPrivacy());
            UserPostDto userPostDto = userPostMapper.toDto(userPostRepository.save(userPost));

            for (MultipartFile file : files) {
                PostImage postImage = new PostImage();
                postImage.setPostId(id);
                postImage.setUrl(uploadFileToS3("2502-post-image", userId, file));
                postImageService.createImage(postImage);
            }
            userPostDto.setImage(postImageService.getAllImageByPostId(id,Status.ACT.toString()));
            return userPostDto;
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),MessageCodes.USERPOST_NOTBELONG,
                    getMessageCode(MessageCodes.USERPOST_NOTBELONG,request));
        }

    }
}
