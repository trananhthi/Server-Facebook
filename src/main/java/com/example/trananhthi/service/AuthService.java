package com.example.trananhthi.service;

import com.example.trananhthi.dto.request.ConfirmCodeRequest;
import com.example.trananhthi.dto.request.SignInRequest;
import com.example.trananhthi.dto.response.SignInResponse;
import com.example.trananhthi.entity.UserAccount;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UserAccount signUpNewAccount(UserAccount userAccount, HttpServletRequest request);

    SignInResponse signIn(SignInRequest signInRequest, HttpServletRequest request);

    SignInResponse refreshToken(String refreshToken, HttpServletRequest request);

    void resendConfirmCode(String email, HttpServletRequest request);

    void confirmSignUpAccount(ConfirmCodeRequest confirmCodeRequest, HttpServletRequest request);
}
