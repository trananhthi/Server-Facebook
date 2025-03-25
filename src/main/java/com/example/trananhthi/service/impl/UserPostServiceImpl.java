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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.trananhthi.util.Utils.getVideoDimensions;

@Service
@RequiredArgsConstructor
@EnableCaching
public class UserPostServiceImpl extends BaseServiceImpl<UserPost,UserPostRepository> implements UserPostService {
    private final UserPostRepository userPostRepository;
    private final PostMediaService postMediaService;
    private final PostImageRepository postImageRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserPostMapper userPostMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UserPostServiceImpl.class);

    @Override
    @SneakyThrows
    @Transactional
    public UserPostDto createNewPost(CreatePostDto dto, List<MultipartFile> imageFiles, List<Integer> imageIndexes,
                                     List<MultipartFile> videoFiles,  List<Integer> videoIndexes, HttpServletRequest request)
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
        userPost.setPrivacy(dto .getPrivacy());
        userPost.setParentPost(dto.getParentPost());
        userPost.setHashtag(dto.getHashtag());
        userPost.setTag(dto.getTag());
        userPost.setAuthor(author);

        userPostRepository.save(userPost);
        //xoa cache
        redisTemplate.delete("allPost::Page request [number: 0, size 5, sort: UNSORTED]");
        if (dto.getTypePost() == TypePost.TEXT) {
            return userPostMapper.toDto(userPost);
        }

        // Upload ảnh & video bất đồng bộ
        CompletableFuture<List<PostMedia>> imageUploadFuture = uploadImagesAsync(userPostId, userId, imageFiles, imageIndexes);
        CompletableFuture<List<PostMedia>> videoUploadFuture = uploadVideosAsync(userPostId, userId, videoFiles, videoIndexes);

        // Chờ cả hai upload xong
        CompletableFuture.allOf(imageUploadFuture, videoUploadFuture).join();

        // Lưu vào database
        postImageRepository.saveAll(imageUploadFuture.join());
        postImageRepository.saveAll(videoUploadFuture.join());

        UserPostDto userPostDto = userPostMapper.toDto(userPost);

        List<PostMediaDto> postMediaDto = postMediaService.findAllMediaByPostId(userPostId, Status.ACT.toString());
        userPostDto.setMediaList(postMediaDto);

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
    @Transactional
    public UserPostDto updateUserPostById(String id, CreatePostDto dto, List<MultipartFile> imageFiles, List<Integer> imageIndexes,
                                          List<MultipartFile> videoFiles, List<Integer> videoIndexes, HttpServletRequest request)
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

            // Upload ảnh & video bất đồng bộ
            CompletableFuture<List<PostMedia>> imageUploadFuture = uploadImagesAsync(id, userId, imageFiles, imageIndexes);
            CompletableFuture<List<PostMedia>> videoUploadFuture = uploadVideosAsync(id, userId, videoFiles, videoIndexes);

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
    public CompletableFuture<List<PostMedia>> uploadImagesAsync(String postId, String userId, List<MultipartFile> files, List<Integer> indexes) {
        return CompletableFuture.supplyAsync(() -> {
            if (files == null || files.isEmpty()) return Collections.emptyList();

            return files.stream().map(file -> {
                String url = uploadFileToS3("2502-post-image", userId, file);
                PostMedia postMedia = new PostMedia();
                postMedia.setPostId(postId);
                postMedia.setUrl(url);
                postMedia.setType(MediaType.IMAGE);
                postMedia.setSize((int) file.getSize() / 1024);
                postMedia.setVisualIndex(indexes.get(files.indexOf(file)));
                try {
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    if (image != null) {
                        postMedia.setWidth(image.getWidth());
                        postMedia.setHeight(image.getHeight());
                    }
                } catch (IOException e) {
                    logger.error("Error processing file: ", e);
                }
                return postMedia;
            }).collect(Collectors.toList());
        });
    }

    @Async
    public CompletableFuture<List<PostMedia>> uploadVideosAsync(String postId, String userId, List<MultipartFile> files, List<Integer> indexes) {
        return CompletableFuture.supplyAsync(() -> {
            if (files == null || files.isEmpty()) return Collections.emptyList();

            return files.stream().map(file -> {
                String url = uploadFileToS3("2502-post-video", userId, file);
                PostMedia postMedia = new PostMedia();
                postMedia.setPostId(postId);
                postMedia.setUrl(url);
                postMedia.setType(MediaType.VIDEO);
                postMedia.setSize((int) file.getSize() / 1024);
                postMedia.setVisualIndex(indexes.get(files.indexOf(file)));
                int[] dimensions = getVideoDimensions(file);
                postMedia.setWidth(dimensions[0]);
                postMedia.setHeight(dimensions[1]);
                return postMedia;
            }).collect(Collectors.toList());
        });
    }

}
