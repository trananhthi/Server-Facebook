package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.dto.PostMediaDto;
import com.example.trananhthi.dto.RestPage;
import com.example.trananhthi.dto.UserPostDto;
import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.entity.PostMedia;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.enumtype.MediaType;
import com.example.trananhthi.enumtype.Privacy;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.enumtype.TypePost;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.mapstruct.UserPostMapper;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.repository.PostImageRepository;
import com.example.trananhthi.repository.UserAccountRepository;
import com.example.trananhthi.repository.UserPostRepository;
import com.example.trananhthi.service.PostMediaService;
import com.example.trananhthi.service.UserPostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableCaching
public class UserPostServiceImpl extends BaseServiceImpl<UserPost,UserPostRepository> implements UserPostService {
    private final UserPostRepository userPostRepository;
    private final PostMediaService postMediaService;
    private final PostImageRepository postImageRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserPostMapper userPostMapper;

    @Override
    @SneakyThrows
    public UserPostDto createNewPost(CreatePostDto dto, List<MultipartFile> imageFiles,
                                     List<MultipartFile> videoFiles, HttpServletRequest request)
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
        userPost.setTypePost(Enum.valueOf(TypePost.class,dto.getTypePost()));
        userPost.setPrivacy(Enum.valueOf(Privacy.class,dto.getPrivacy()));
        userPost.setParentPost(dto.getParentPost());
        userPost.setHashtag(dto.getHashtag());
        userPost.setTag(dto.getTag());
        userPost.setAuthor(author);

        userPostRepository.save(userPost);

        // Upload ảnh & video bất đồng bộ
        CompletableFuture<List<PostMedia>> imageUploadFuture = uploadImagesAsync(userPostId, userId, imageFiles);
        CompletableFuture<List<PostMedia>> videoUploadFuture = uploadVideosAsync(userPostId, userId, videoFiles);

        // Chờ cả hai upload xong
        CompletableFuture.allOf(imageUploadFuture, videoUploadFuture).join();

        // Lưu vào database
        postImageRepository.saveAll(imageUploadFuture.join());
        postImageRepository.saveAll(videoUploadFuture.join());

        UserPostDto userPostDto = userPostMapper.toDto(userPost);

        if(!userPost.getTypePost().equals(TypePost.TEXT)) {
            List<PostMediaDto> postMediaDto = postMediaService.findAllMediaByPostId(userPostId, Status.ACT.toString());
            userPostDto.setMediaList(postMediaDto);
        }

        return userPostDto;
    }

    @Override
    @Cacheable("allPost")
    public RestPage<UserPostDto> getAllPost(Pageable pageable)
    {
        Page<UserPost> userPostPage = userPostRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<UserPostDto> userPostDTOList = userPostPage.map(userPost -> {
            UserPostDto userPostDto = userPostMapper.toDto(userPost);
            userPostDto.setMediaList(postMediaService.findAllMediaByPostId(userPostDto.getId(), Status.ACT.toString()));
            return userPostDto;
        });
        return new RestPage<>(userPostDTOList);
    }

    @Override
    public UserPostDto updateUserPostById(String id, CreatePostDto dto, List<MultipartFile> imageFiles,
                                          List<MultipartFile> videoFiles, HttpServletRequest request)
    {
        String userId = UserContext.getUserId();
        UserPost userPost = userPostRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND.value(), MessageCodes.USERPOST_NOTFOUND ,
                        getMessageCode(MessageCodes.USERPOST_NOTFOUND, request, id)));
        if(userPost.getAuthor().getId().equals(userId))
        {
            userPost.setContent(dto.getContent());
            userPost.setTypePost(Enum.valueOf(TypePost.class,dto.getTypePost()));
            userPost.setPrivacy(Enum.valueOf(Privacy.class,dto.getPrivacy()));
            UserPostDto userPostDto = userPostMapper.toDto(userPostRepository.save(userPost));

            // Upload ảnh & video bất đồng bộ
            CompletableFuture<List<PostMedia>> imageUploadFuture = uploadImagesAsync(id, userId, imageFiles);
            CompletableFuture<List<PostMedia>> videoUploadFuture = uploadVideosAsync(id, userId, videoFiles);

            // Chờ cả hai upload xong
            CompletableFuture.allOf(imageUploadFuture, videoUploadFuture).join();

            // Lưu vào database
            postImageRepository.saveAll(imageUploadFuture.join());
            postImageRepository.saveAll(videoUploadFuture.join());

            if(!userPost.getTypePost().equals(TypePost.TEXT)) {
                List<PostMediaDto> postMediaDto = postMediaService.findAllMediaByPostId(id, Status.ACT.toString());
                userPostDto.setMediaList(postMediaDto);
            }

            return userPostDto;
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),MessageCodes.USERPOST_NOTBELONG,
                    getMessageCode(MessageCodes.USERPOST_NOTBELONG,request));
        }

    }

    @Async
    public CompletableFuture<List<PostMedia>> uploadImagesAsync(String postId, String userId, List<MultipartFile> files) {
        return CompletableFuture.supplyAsync(() -> {
            if (files == null || files.isEmpty()) return Collections.emptyList();

            return files.stream().map(file -> {
                String url = uploadFileToS3("2502-post-image", userId, file);
                PostMedia postMedia = new PostMedia();
                postMedia.setPostId(postId);
                postMedia.setUrl(url);
                postMedia.setType(MediaType.IMAGE);
                postMedia.setSize((int) file.getSize() / 1024);
                return postMedia;
            }).collect(Collectors.toList());
        });
    }

    @Async
    public CompletableFuture<List<PostMedia>> uploadVideosAsync(String postId, String userId, List<MultipartFile> files) {
        return CompletableFuture.supplyAsync(() -> {
            if (files == null || files.isEmpty()) return Collections.emptyList();

            return files.stream().map(file -> {
                String url = uploadFileToS3("2502-post-video", userId, file);
                PostMedia postMedia = new PostMedia();
                postMedia.setPostId(postId);
                postMedia.setUrl(url);
                postMedia.setType(MediaType.VIDEO);
                postMedia.setSize((int) file.getSize() / 1024);
                return postMedia;
            }).collect(Collectors.toList());
        });
    }

}
