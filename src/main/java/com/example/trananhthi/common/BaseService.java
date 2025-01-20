package com.example.trananhthi.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface BaseService {
    String getMessageCode(String code, HttpServletRequest request);

    <T> Page<T> createPageFromList(List<T> data, Pageable pageable);

    String uploadFileToS3(String bucketName, String folder, MultipartFile file) throws IOException;
}
