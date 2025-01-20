package com.example.trananhthi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    private int statusCode;
    private String errorKey;
    private String message;
    private String date;

    public CustomErrorResponse(int statusCode, String errorKey, String message) {
        // Lấy ngày hiện tại
        LocalDateTime currentDate = LocalDateTime.now();

        // Định dạng ngày theo định dạng mong muốn
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.statusCode = statusCode;
        this.errorKey = errorKey;
        this.message = message;
        this.date = currentDate.format(formatter);
    }
}
