package com.example.trananhthi.controller;

import com.example.trananhthi.common.CustomSuccessResponse;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/post-image")
public class PostImageController {
    private final PostImageService postImageService;

    @Autowired
    public PostImageController(PostImageService postImageService) {
        this.postImageService = postImageService;
    }

    @PatchMapping("/delete/{imageID}")
    public ResponseEntity<?> deleteImage (@PathVariable Long imageID)
    {
        if(postImageService.deleteImage(imageID))
        {
            return ResponseEntity.ok().body(new CustomSuccessResponse("Đã xóa thành công","success"));
        }
        else {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "ImageIsNotDeleted","Xóa không thành công");
        }
    }
}
