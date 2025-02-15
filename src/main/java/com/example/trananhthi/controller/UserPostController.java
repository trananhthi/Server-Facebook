package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.dto.request.CreatePostDto;
import com.example.trananhthi.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserPostController extends BaseController {
    private final UserPostService userPostService;
    private static final String ROOT = "/post";

    @PostMapping(V1 + ROOT + "/create")
    public ResponseEntity<?> createPost(@RequestParam(name = "data") String dataJson,
                                        @RequestParam(name = "imageFiles") List<MultipartFile> imageFiles,
                                        @RequestParam(name = "videoFiles") List<MultipartFile> videoFiles,
                                        HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        CreatePostDto dto = mapper.readValue(dataJson, CreatePostDto.class);
        return new ResponseEntity<>(userPostService.createNewPost(dto, imageFiles, videoFiles, request), HttpStatus.CREATED);
    }

    @GetMapping(V1 + ROOT + "/get")
    public ResponseEntity<?> getAllPost(@RequestParam(defaultValue = "-1") int page, @RequestParam(defaultValue = "0")  int size)
    {
        Pageable pageable;
        if(page == -1 || size == 0)
        {
            pageable = Pageable.unpaged();
        }
        else{
            pageable = PageRequest.of(page,size);
        }
        return ResponseEntity.ok().body(userPostService.getAllPost(pageable));
    }

    @PatchMapping(V1 + ROOT + "/update/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable String postId,
                                        @RequestParam(name = "data") String dataJson,
                                        @RequestParam(name = "imageFiles") List<MultipartFile> imageFiles,
                                        @RequestParam(name = "videoFiles") List<MultipartFile> videoFiles,
                                        HttpServletRequest request ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        CreatePostDto dto = mapper.readValue(dataJson, CreatePostDto.class);

        return new ResponseEntity<>(userPostService.updateUserPostById(postId, dto, imageFiles, videoFiles, request), HttpStatus.OK);
    }
}
