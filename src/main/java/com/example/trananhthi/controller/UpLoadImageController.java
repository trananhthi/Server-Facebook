package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UpLoadImageController extends BaseController {
    private final S3Service s3Service;
    private static final String ROOT = "/upLoadImage";

    @PostMapping(V1 + ROOT + "/avatar")
    public String upLoadAvatar(@RequestBody MultipartFile file) throws IOException {
        return s3Service.uploadImageToS3("2502-facebook",file);
    }
}
