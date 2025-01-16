package com.example.trananhthi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    String uploadImageToS3(String bucketName, MultipartFile image) throws IOException;

    void deleteImageFromS3(String bucketName,String imageName);

}
