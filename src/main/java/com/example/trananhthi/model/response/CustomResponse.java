package com.example.trananhthi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class CustomResponse {
    private String key;
    private String message;
    private String date;

    public CustomResponse(String key, String message) {
        // Lấy ngày hiện tại
        LocalDateTime currentDate = LocalDateTime.now();

        // Định dạng ngày theo định dạng mong muốn
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.key = key;
        this.message = message;
        this.date = currentDate.format(formatter);
    }
}
