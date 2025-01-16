package com.example.trananhthi.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface BaseService {
    String getMessageCode(String code, HttpServletRequest request);
}
