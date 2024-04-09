package com.example.trananhthi.controller;

import com.example.trananhthi.common.*;
import com.example.trananhthi.component.Base64Encoding;
import com.example.trananhthi.entity.ConfirmCode;
import com.example.trananhthi.entity.RefreshToken;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.exception.TokenRefreshException;
import com.example.trananhthi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/v1/authenticate")
public class AuthController {
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final ConfirmCodeService confirmCodeService;
    private final MyEmailService myEmailService;
    private final Environment env;

    @Autowired
    public AuthController(UserAccountService userAccountService, JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, ConfirmCodeService confirmCodeService, MyEmailService myEmailService, Environment env) {
        this.userAccountService = userAccountService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.confirmCodeService = confirmCodeService;
        this.myEmailService = myEmailService;
        this.env = env;
    }

    @PostMapping("/signup")
    public ResponseEntity<CustomSuccessResponse> signUpNewAccount(@RequestBody UserAccount userAccount)
    {
        ResponseEntity<CustomSuccessResponse> response = null;
        UserAccount newAccount = userAccountService.signUpNewAccount(userAccount);
        if(newAccount.getId() > 0)
        {
            String key = Base64Encoding.encodeStringToBase64(userAccount.getEmail());
            sendConfirmCodeEmail(userAccount);
            response = ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CustomSuccessResponse("Đăng kí tài khoản " + userAccount.getEmail() + " thành công",key));
        }
        return  response;
    }

    @PostMapping("/signin")
    public ResponseEntity<SignUpResponse> signIn(@RequestBody SignUpRequest signUpRequest)
    {
        if(signUpRequest.getEmail().isEmpty() || signUpRequest.getPassword().isEmpty())
        {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),"LackOfEmailOrPassword","Thiếu Email hoặc mật khẩu");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));
        Object principal = authentication.getPrincipal();
        Optional<UserAccount> userAccount = userAccountService.getUserByEmail(((UserDetails) principal).getUsername());
        if(userAccount.isPresent() && userAccount.get().getStatus().equals("not_activated"))
        {
            return ResponseEntity.status(HttpStatus.OK).body(new SignUpResponse("","","Tài khoản chưa xác thực email", Base64Encoding.encodeStringToBase64(signUpRequest.getEmail())));
        }
        else if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(signUpRequest.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new SignUpResponse(jwtService.generateToken(signUpRequest.getEmail()),refreshToken.getToken())) ;
        } else {
            throw new UsernameNotFoundException("invalid user with email = " + signUpRequest.getEmail() + " request !");
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest)
    {
        String refreshToken = tokenRefreshRequest.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserAccount)
                .map(userAccount -> {
                    String token = jwtService.generateToken(userAccount.getEmail());
                    return ResponseEntity.ok().body(new SignUpResponse(token,refreshToken));
                })
                .orElseThrow(()->
                    new TokenRefreshException("RefreshTokenIsInexist","Refresh token không tồn tại")
                );

    }

    @PostMapping("/confirm")
    public ResponseEntity<CustomSuccessResponse> confirmSignUpAccount(@RequestBody ConfirmCodeRequest confirmCodeRequest)
    {
        Optional<ConfirmCode> confirmCode = confirmCodeService.findByCode(confirmCodeRequest.getCode());
        if(confirmCode.isPresent())
        {
            if(confirmCodeService.isExpiredCode(confirmCode.get()))
            {
                throw new CustomException(HttpStatus.BAD_REQUEST.value(),"ConfirmCodeIsExpired","Mã xác nhận quá hạn");
            }
            else{
                String email = confirmCode.get().getUserAccount().getEmail();
                String emailRequest = Base64Encoding.decodeBase64ToString(confirmCodeRequest.getEmail());
                if(emailRequest.equals(email))
                {
                    userAccountService.updateStatusByEmail(email,"activated");
                    confirmCodeService.deleteByCode(confirmCodeRequest.getCode());
                    return ResponseEntity.ok().body(new CustomSuccessResponse("Xác nhận tài khoản thành công"));
                }
                else{
                    throw new CustomException(HttpStatus.BAD_REQUEST.value(), "ConfirmCodeIsIncorrect","Mã xác nhận sai");
                }
            }
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "ConfirmCodeIsIncorrect","Mã xác nhận sai");
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<CustomSuccessResponse> resendConfirmCode(@Param("email") String email)
    {
        String emailRequest = Base64Encoding.decodeBase64ToString(email);
        Optional<UserAccount> userAccount = userAccountService.getUserByEmail(emailRequest);
        if(userAccount.isEmpty())
        {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "InexistAccount","Tài khoản không tồn tại");
        }
        if(userAccount.get().getStatus().equals("activated"))
        {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "VerifiedAccount","Tài khoản đã xác thực email");
        }
        confirmCodeService.deleteAllByUserAccount(userAccount.get());
        sendConfirmCodeEmail(userAccount.get());
        return ResponseEntity.ok().body(new CustomSuccessResponse("Gửi lại mã xác nhận thành công"));
    }

    private void sendConfirmCodeEmail(UserAccount userAccount)
    {
        ConfirmCode confirmCode=  confirmCodeService.createConfirmCode(userAccount.getEmail());
        String confirmationUrl =env.getProperty("client.URL")
                +"/authenticate/"+ Base64Encoding.encodeStringToBase64(userAccount.getEmail());

        String to = userAccount.getEmail();
        String subject =confirmCode.getCode()+ " là mã xác nhận Facebook của bạn";
        StringBuilder body = new StringBuilder();
        String htmlContent = readEmailTemplate();
        htmlContent = htmlContent.replace("${lastName}",userAccount.getLastName());
        htmlContent = htmlContent.replace("${email}",userAccount.getEmail());
        htmlContent = htmlContent.replace("${code}",confirmCode.getCode());
        htmlContent = htmlContent.replace("${confirmUrl}",confirmationUrl);
        body.append(htmlContent);

        new Thread(()->{
            try
            {
                myEmailService.sendSimpleMessage(to,subject,body.toString());
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }).start();
    }
    private String readEmailTemplate() {
        Resource resource = new ClassPathResource("static/emailTemplate.html");
        try {
            byte[] byteData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(byteData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Xử lý lỗi
            System.out.println(e.getMessage());
            return ""; // Hoặc trả về một giá trị mặc định
        }
    }
}
