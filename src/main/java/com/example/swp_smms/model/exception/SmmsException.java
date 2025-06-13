package com.example.swp_smms.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SmmsException extends RuntimeException {
    private final HttpStatus httpStatus;

    public SmmsException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
} 