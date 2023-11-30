package com.example.trananhthi.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmCodeRequest {
    private String email;
    private String code;
}
