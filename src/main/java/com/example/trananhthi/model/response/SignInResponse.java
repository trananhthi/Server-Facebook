package com.example.trananhthi.model.response;

import com.example.trananhthi.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private UserAccountDto userInfo;
    private String accessToken;
    private String refreshToken;
    private String message;
    private String key;

    public SignInResponse(String accessToken, String refreshToken)
    {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
