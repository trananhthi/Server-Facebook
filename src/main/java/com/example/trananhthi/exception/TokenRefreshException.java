package com.example.trananhthi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
@Setter
public class TokenRefreshException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private String errorKey;

    public TokenRefreshException(String errorKey,String message) {
        super(message);
        this.errorKey = errorKey;
    }
}
