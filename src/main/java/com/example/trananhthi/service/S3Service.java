package com.example.trananhthi.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.trananhthi.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    private final Environment env;
    private final Utils utils;
    @Autowired
    public S3Service(AmazonS3 amazonS3, Environment env, Utils utils) {
        this.amazonS3 = amazonS3;
        this.env = env;
        this.utils = utils;
    }

    public String uploadImageToS3(String bucketName,MultipartFile image) throws IOException {
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

    public void deleteImageFromS3(String bucketName,String imageName)
    {
        amazonS3.deleteObject(bucketName,imageName);
    }
}
