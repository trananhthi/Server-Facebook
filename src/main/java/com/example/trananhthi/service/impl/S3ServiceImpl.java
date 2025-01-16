package com.example.trananhthi.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.trananhthi.service.S3Service;
import com.example.trananhthi.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 amazonS3;
    private final Environment env;
    private final Utils utils;

    @Override
    public String uploadImageToS3(String bucketName, MultipartFile image) throws IOException {
        String imageUrl;
        String fileName = utils.generateFileName(image);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());
        try{
            amazonS3.putObject(new PutObjectRequest(bucketName,fileName,image.getInputStream(), metadata));
            imageUrl = Objects.requireNonNull(env.getProperty("aws.s3.endpoint")) + "/" + bucketName + "/" + fileName;
        }
        catch (AmazonServiceException e)
        {
            throw new IllegalStateException("Failed to upload the file", e);
        }
        return imageUrl;
    }

    @Override
    public void deleteImageFromS3(String bucketName,String imageName)
    {
        amazonS3.deleteObject(bucketName,imageName);
    }
}
