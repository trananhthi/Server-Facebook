package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.model.response.CustomResponse;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.service.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostMediaController extends BaseController {
    private final PostMediaService postMediaService;
    private static final String ROOT = "/post-media";

    @PatchMapping(V1 + ROOT + "/delete/{imageId}")
    public ResponseEntity<?> deleteImage (@PathVariable String imageId)
    {
        if(postMediaService.deleteMedia(imageId))
        {
            return ResponseEntity.ok().body(new CustomResponse("Đã xóa thành công","success"));
        }
        else {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "ImageIsNotDeleted","Xóa không thành công");
        }
    }
}
