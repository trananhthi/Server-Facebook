package com.example.trananhthi.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class AmazonS3Config {

    private final Environment env;
    @Autowired
    public AmazonS3Config(Environment env) {
        this.env = env;
    }

    @Bean
    public AmazonS3 amazonS3()
    {
        AWSCredentials awsCredentials = new BasicAWSCredentials(Objects.requireNonNull(env.getProperty("aws.accessKey")), Objects.requireNonNull(env.getProperty("aws.secretKey")));
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(Objects.requireNonNull(env.getProperty("aws.s3.endpoint")),Objects.requireNonNull(env.getProperty("aws.s3.region"))))
//                .withRegion(Regions.DEFAULT_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

}
