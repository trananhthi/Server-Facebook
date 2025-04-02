package com.example.trananhthi.exception;

import com.example.trananhthi.model.response.CustomResponse;
import com.example.trananhthi.message.MessageCodes;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    public ResponseEntity<CustomResponse> handleUnwantedException(Exception e, HttpServletRequest request) {
        logger.error("Unhandled exception at {}: ", request.getRequestURI(), e);
        String message = messageSource.getMessage(MessageCodes.INTERNAL_SERVER_ERROR, null, request.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse(MessageCodes.INTERNAL_SERVER_ERROR,message));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        String message = messageSource.getMessage(MessageCodes.ENDPOINT_NOTFOUND, new Object[]{ex.getRequestURL()}, request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CustomResponse(MessageCodes.ENDPOINT_NOTFOUND, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse> handleBodyIsMissingException(HttpMessageNotReadableException e)
    {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse("BodyRequestNotFound","Thiếu body request"));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse> handleCustomException(CustomException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(e.getErrorCode()).body(new CustomResponse(e.getErrorKey(),e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponse> handleSignInException(Exception e, HttpServletRequest request)
    {
        logger.error(e.getMessage());
        String message = messageSource.getMessage(MessageCodes.EMAIL_OR_PASSWORD_INCORRECT, null, request.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse(
                MessageCodes.EMAIL_OR_PASSWORD_INCORRECT,message));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<CustomResponse> handleTokenRefreshException(TokenRefreshException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomResponse(
                e.getErrorKey(),
                e.getMessage()));
    }
}
