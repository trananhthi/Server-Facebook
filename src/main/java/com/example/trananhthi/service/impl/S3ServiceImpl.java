package com.example.trananhthi.service.impl;

import com.example.trananhthi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client;
    private final S3AsyncClient s3AsyncClient;
    private final Environment env;

    @Override
    public void deleteImageFromS3(String bucketName, String imageName) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageName)
                    .build();

            s3Client.deleteObject(deleteRequest);

            System.out.println("Deleted: " + imageName);

        } catch (S3Exception e) {
            throw new IllegalStateException("Failed to delete the image from S3", e);
        }
    }

    @Override
    public CompletableFuture<Void> deleteImageFromS3Async(String bucketName, String imageName) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageName)
                    .build();

            return s3AsyncClient.deleteObject(deleteRequest)
                    .thenRun(() -> System.out.println("Deleted (async): " + imageName))
                    .exceptionally(e -> {
                        throw new IllegalStateException("Failed to delete the image from S3", e);
                    });

        } catch (S3Exception e) {
            throw new IllegalStateException("Error preparing image deletion", e);
        }
    }
}
