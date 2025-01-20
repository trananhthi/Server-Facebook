package com.example.trananhthi.exception;

import com.example.trananhthi.dto.response.CustomErrorResponse;
import com.example.trananhthi.message.MessageCodes;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger("FacebookSever");
    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleUnwantedException(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"UnknownError","Lỗi không xác định"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        String message = messageSource.getMessage(MessageCodes.ENDPOINT_NOTFOUND, new Object[]{ex.getRequestURL()}, request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), MessageCodes.ENDPOINT_NOTFOUND, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorResponse> handleBodyIsMissingException(HttpMessageNotReadableException e)
    {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), "BodyRequestNotFound","Thiếu body request"));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(e.getErrorCode()).body(new CustomErrorResponse(e.getErrorCode(), e.getErrorKey(),e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleSignInException(Exception e)
    {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),"EmailOrPasswordInValid","Email hoặc mật khẩu không đúng"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDuplicateEmailSignUpException(DataIntegrityViolationException e)
    {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),"EmailAlreadyExist","Email đã tồn tại"));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<CustomErrorResponse> handleTokenRefreshException(TokenRefreshException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                e.getErrorKey(),
                e.getMessage()));
    }
}
