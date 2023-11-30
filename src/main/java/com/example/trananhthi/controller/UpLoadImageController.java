package com.example.trananhthi.controller;

import com.example.trananhthi.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/upLoadImage")
public class UpLoadImageController {
    private final S3Service s3Service;
    @Autowired
    public UpLoadImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/avatar")
    public String upLoadAvatar(@RequestBody MultipartFile file) throws IOException {
        return s3Service.uploadImageToS3("2502-facebook",file);
    }
}
