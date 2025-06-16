package com.example.swp_smms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SmmsException extends RuntimeException {
    private final HttpStatus status;

    public SmmsException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
} 