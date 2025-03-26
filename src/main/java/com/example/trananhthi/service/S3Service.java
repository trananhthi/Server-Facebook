package com.example.trananhthi.service;

import java.util.concurrent.CompletableFuture;

public interface S3Service {
    void deleteImageFromS3(String bucketName,String imageName);

    CompletableFuture<Void> deleteImageFromS3Async(String bucketName, String imageName);
}
