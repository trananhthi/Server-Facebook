package com.example.trananhthi.service.impl;

import com.example.trananhthi.component.Base64Encoding;
import com.example.trananhthi.dto.request.ConfirmCodeRequest;
import com.example.trananhthi.dto.request.SignInRequest;
import com.example.trananhthi.dto.response.SignInResponse;
import com.example.trananhthi.entity.ConfirmCode;
import com.example.trananhthi.entity.RefreshToken;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.mapstruct.UserAccountMapper;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.repository.UserAccountRepository;
import com.example.trananhthi.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.trananhthi.util.Utils.readEmailTemplate;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final ConfirmCodeService confirmCodeService;
    private final MyEmailService myEmailService;
    private final Environment env;

    @Autowired
    public AuthServiceImpl(UserAccountRepository userAccountRepository, UserAccountMapper userAccountMapper, @Lazy PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, MessageSource messageSource, RefreshTokenService refreshTokenService, JwtService jwtService, ConfirmCodeService confirmCodeService, MyEmailService myEmailService, Environment env) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountMapper = userAccountMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.messageSource = messageSource;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.confirmCodeService = confirmCodeService;
        this.myEmailService = myEmailService;
        this.env = env;
    }

    @Override
    public UserAccount signUpNewAccount(UserAccount userAccount, HttpServletRequest request)
    {
        Optional<UserAccount> existedEmail = userAccountRepository.findByEmail(userAccount.getEmail());
        if(existedEmail.isPresent())
        {
            String message = messageSource.getMessage(MessageCodes.EMAIL_EXISTED, new Object[]{userAccount.getEmail()}, request.getLocale());
            throw new CustomException(HttpStatus.CONFLICT.value(), MessageCodes.EMAIL_EXISTED, message);
        }
        String encodePassword = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(encodePassword);
        userAccount.setRole("ROLE_USER");
        userAccount.setStatus(Status.TEM);
        userAccount.setName(userAccount.getFirstName() + ' ' + userAccount.getLastName());
        userAccount.setAvatar("https://s3-hcm-r1.longvan.net/2502-facebook/default_avatar.png");

        try
        {
            UserAccount newAccount = userAccountRepository.save(userAccount);
            sendConfirmCodeEmail(newAccount);
            return newAccount;
        }
        catch (Exception e)
        {
            String message = messageSource.getMessage(MessageCodes.SIGNUP_FAILED,new Object[]{userAccount.getEmail()}, request.getLocale());
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),MessageCodes.SIGNUP_FAILED, message);
        }
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest, HttpServletRequest request) {
        SignInResponse signInResponse = new SignInResponse();

        if(signInRequest.getEmail() == null || signInRequest.getPassword() == null || signInRequest.getEmail().isBlank() || signInRequest.getPassword().isBlank())
        {
            String message = messageSource.getMessage(MessageCodes.LACKOF_EMAIL_OR_PASSWORD, null, request.getLocale());
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),MessageCodes.LACKOF_EMAIL_OR_PASSWORD,message);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        Object principal = authentication.getPrincipal();
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(((UserDetails) principal).getUsername());

        if(userAccount.isPresent() && userAccount.get().getStatus().equals(Status.TEM))
        {
            String message = messageSource.getMessage(MessageCodes.EMAIL_NOTCONFIRMED, new Object[]{signInRequest.getEmail()}, request.getLocale());
            signInResponse.setMessage(message);
            signInResponse.setKey(Base64Encoding.encodeStringToBase64(signInRequest.getEmail()));
            return signInResponse;
        }
        if(userAccount.isEmpty() || !authentication.isAuthenticated())
        {
            String message = messageSource.getMessage(MessageCodes.EMAIL_OR_PASSWORD_INCORRECT, null, request.getLocale());
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),MessageCodes.EMAIL_OR_PASSWORD_INCORRECT,message);
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(signInRequest.getEmail());
        String accessToken = jwtService.generateToken(signInRequest.getEmail(),userAccount.get().getId(), userAccount.get().getRole());

        System.out.println(userAccountMapper.toDto(userAccount.get()).getFirstName());
        signInResponse.setUserInfo(userAccountMapper.toDto(userAccount.get()));
        signInResponse.setAccessToken(accessToken);
        signInResponse.setRefreshToken(refreshToken.getToken());
        signInResponse.setMessage("Đăng nhập thành công");
        signInResponse.setKey("Success");
        return signInResponse;
    }

    @Override
    public SignInResponse refreshToken(String refreshToken, HttpServletRequest request) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserAccount)
                .map(userAccount -> {
                    String token = jwtService.generateToken(userAccount.getEmail(),userAccount.getId(),userAccount.getRole());
                    return new SignInResponse(token,refreshToken);
                })
                .orElseThrow(()->
                        new CustomException(HttpStatus.BAD_REQUEST.value(),MessageCodes.REFRESH_TOKEN_ISINEXISTENT,
                                messageSource.getMessage(MessageCodes.REFRESH_TOKEN_ISINEXISTENT, null, request.getLocale()))
                );
    }

    @Override
    public void resendConfirmCode(String email, HttpServletRequest request) {
        String emailRequest = Base64Encoding.decodeBase64ToString(email);
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(emailRequest);
        if(userAccount.isEmpty())
        {
            String message = messageSource.getMessage(MessageCodes.ACCOUNT_NOTEXIST, new Object[]{emailRequest}, request.getLocale());
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.ACCOUNT_NOTEXIST, message);
        }
        if(userAccount.get().getStatus().equals(Status.ACT))
        {
            String message = messageSource.getMessage(MessageCodes.ACCOUNT_EMAILCONFIRMED, null, request.getLocale());
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.ACCOUNT_EMAILCONFIRMED, message);
        }
        confirmCodeService.deleteAllByUserAccount(userAccount.get());
        sendConfirmCodeEmail(userAccount.get());
    }

    @Override
    public void confirmSignUpAccount(ConfirmCodeRequest confirmCodeRequest, HttpServletRequest request) {
        Optional<ConfirmCode> confirmCode = confirmCodeService.findByCode(confirmCodeRequest.getCode());
        if(confirmCode.isPresent())
        {
            if(confirmCodeService.isExpiredCode(confirmCode.get()))
            {
                String message = messageSource.getMessage(MessageCodes.CONFIRMCODE_EXPIRED, null, request.getLocale());
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.CONFIRMCODE_EXPIRED, message);
            }
            else{
                String email = confirmCode.get().getUserAccount().getEmail();
                String emailRequest = Base64Encoding.decodeBase64ToString(confirmCodeRequest.getEmail());
                if(emailRequest.equals(email))
                {
                    userAccountRepository.updateStatusByEmail(email, Status.ACT.toString());
                    confirmCodeService.deleteByCode(confirmCodeRequest.getCode());
                }
                else{
                    String message = messageSource.getMessage(MessageCodes.CONFIRMCODE_INCORRECT, null, request.getLocale());
                    throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.CONFIRMCODE_INCORRECT, message);
                }
            }
        }
        else{
            String message = messageSource.getMessage(MessageCodes.CONFIRMCODE_INCORRECT, null, request.getLocale());
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.CONFIRMCODE_INCORRECT, message);
        }
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
}
