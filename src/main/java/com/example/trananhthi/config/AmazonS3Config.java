package com.example.trananhthi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.Objects;

@Configuration
public class AmazonS3Config {

    private final Environment env;

    @Autowired
    public AmazonS3Config(Environment env) {
        this.env = env;
    }

    @Bean
    public S3Client s3Client() {
        // Khởi tạo thông tin đăng nhập
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                Objects.requireNonNull(env.getProperty("aws.accessKey")),
                Objects.requireNonNull(env.getProperty("aws.secretKey"))
        );

        return S3Client.builder()
                // Cung cấp credentials
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                // Định cấu hình endpoint (nếu dùng MinIO hoặc S3 custom endpoint)
                .endpointOverride(URI.create(Objects.requireNonNull(env.getProperty("aws.s3.endpoint"))))
                // Chọn region
                .region(Region.of(Objects.requireNonNull(env.getProperty("aws.s3.region"))))
                .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                Objects.requireNonNull(env.getProperty("aws.accessKey")),
                Objects.requireNonNull(env.getProperty("aws.secretKey"))
        );

        return S3AsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(Objects.requireNonNull(env.getProperty("aws.s3.endpoint"))))
                .region(Region.of(Objects.requireNonNull(env.getProperty("aws.s3.region"))))
                .build();
    }
}
