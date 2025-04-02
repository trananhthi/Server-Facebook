package com.example.trananhthi.controller;

import com.example.trananhthi.common.*;
import com.example.trananhthi.component.Base64Encoding;
import com.example.trananhthi.model.response.CustomResponse;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.model.request.ConfirmCodeRequest;
import com.example.trananhthi.model.request.SignInRequest;
import com.example.trananhthi.model.request.TokenRefreshRequest;
import com.example.trananhthi.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final MessageSource messageSource;
    private final AuthService authService;

    private static final String ROOT = "/authenticate";

    @PostMapping(V1 + ROOT+ "/signup")
    public ResponseEntity<?> signUpNewAccount(@RequestBody UserAccount userAccount, HttpServletRequest request)
    {
        UserAccount newAccount = authService.signUpNewAccount(userAccount, request);
        String message = messageSource.getMessage(MessageCodes.SIGNUP_SUCCESS,new Object[]{newAccount.getEmail()}, request.getLocale());
        String key = Base64Encoding.encodeStringToBase64(newAccount.getEmail());
        return new ResponseEntity<>(new CustomResponse(key, message),HttpStatus.CREATED);
    }

    @PostMapping(V1 + ROOT + "/signin")
    public ResponseEntity<?> signIn(@RequestBody(required = false) SignInRequest signInRequest,
                                    HttpServletRequest request)
    {
        return new ResponseEntity<>(authService.signIn(signInRequest,request),HttpStatus.OK);
    }

    @PostMapping(V1 + ROOT + "/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest, HttpServletRequest request)
    {
        return new ResponseEntity<>(authService.refreshToken(tokenRefreshRequest.getRefreshToken(), request),HttpStatus.OK);
    }

    @PostMapping(V1 + ROOT + "/confirm")
    public ResponseEntity<?> confirmSignUpAccount(@RequestBody ConfirmCodeRequest confirmCodeRequest, HttpServletRequest request)
    {
        authService.confirmSignUpAccount(confirmCodeRequest, request);
        String message = messageSource.getMessage(MessageCodes.ACCOUNT_CONFIRMATION_SUCCESS,null, request.getLocale());
        return new ResponseEntity<>(new CustomResponse(MessageCodes.ACCOUNT_CONFIRMATION_SUCCESS, message), HttpStatus.OK);
    }

    @PostMapping(V1 + ROOT + "/resend")
    public ResponseEntity<?> resendConfirmCode(@Param("email") String email, HttpServletRequest request)
    {
        authService.resendConfirmCode(email, request);
        String message = messageSource.getMessage(MessageCodes.RESEND_CONFIRMCODE_SUCCESS,new Object[]{email}, request.getLocale());
        return new ResponseEntity<>(new CustomResponse(MessageCodes.RESEND_CONFIRMCODE_SUCCESS, message), HttpStatus.OK);
    }

}
