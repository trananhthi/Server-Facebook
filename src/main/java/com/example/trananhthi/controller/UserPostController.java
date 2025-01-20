package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.dto.request.CustomSuccessResponse;
import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.dto.UserPostDTO;
import com.example.trananhthi.entity.PostImage;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.entity.UserPost;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserPostController extends BaseController {
    private final UserPostService userPostService;
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private final S3Service s3Service;
    private final PostImageService postImageService;
    private final MapEntityToDTO mapEntityToDTO = MapEntityToDTO.getInstance();
    private static final String ROOT = "/post";

    @PostMapping(V1 + ROOT + "/create")
    public ResponseEntity<?> createPost(@RequestHeader(name = "Authorization") String token,
                                        @ModelAttribute CreatePostDto dto,
                                        @RequestBody List<MultipartFile> files ) throws IOException {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String email = jwtService.extractUsername(jwtToken);
            UserAccount userAccount = userAccountService.getUserByEmail(email).orElse(null);
            UserPost newUserPost = mapEntityToDTO.mapCreatePostDTOToEntity(dto);
            newUserPost.setAuthor(userAccount);
            UserPost newPost = userPostService.createNewPost(newUserPost);
            if (newPost != null && newPost.getTypePost().equals("image"))
            {
                for (MultipartFile file : files) {
                    PostImage postImage = new PostImage();
                    postImage.setUserPost(newPost);
                    postImage.setUrl(s3Service.uploadImageToS3("2502-post-image",file));
                    postImageService.createImage(postImage);
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(new CustomSuccessResponse("Đăng bài thành công","success"));
            }
            else{
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "PostIsNotCreated","Tạo bài đăng không thành công");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping(V1 + ROOT + "/get")
    public ResponseEntity<Page<UserPostDTO>> getAllPost(@RequestParam(defaultValue = "-1") int page, @RequestParam(defaultValue = "0")  int size)
    {
        Pageable pageable;
        if(page == -1 || size == 0)
        {
            pageable = Pageable.unpaged();
        }
        else{
            pageable = PageRequest.of(page,size);
        }
        System.out.println(UserContext.getUserId());
        Page<UserPostDTO> userPostDTOList = userPostService.getAllPost(pageable);
        return ResponseEntity.ok().body(userPostDTOList);
    }

    @PatchMapping(V1 + ROOT + "/update/{postID}")
    public ResponseEntity<?> updatePost(@RequestHeader(name = "Authorization") String token, @PathVariable String postID, @ModelAttribute CreatePostDto dto, @RequestBody List<MultipartFile> files) throws IOException {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String email = jwtService.extractUsername(jwtToken);
            UserPost updatedUserPost = userPostService.updateUserPostByID(postID,email,dto);
            if (updatedUserPost != null)
            {
                for (MultipartFile file : files) {
                    PostImage postImage = new PostImage();
                    postImage.setUserPost(updatedUserPost);
                    postImage.setUrl(s3Service.uploadImageToS3("2502-post-image",file));
                    postImageService.createImage(postImage);
                }
                UserPostDTO userPostDTO = mapEntityToDTO.mapUserPostToDTO(updatedUserPost);
                userPostDTO.setImage(postImageService.getAllImageByPostId(userPostDTO.getId(),"actived"));
                return ResponseEntity.ok().body(userPostDTO);
            }
            else{
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "PostCanNotUpdated","Cập nhật bài viết không thành công");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
