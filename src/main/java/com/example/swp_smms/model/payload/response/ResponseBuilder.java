package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBuilder<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ResponseBuilder<T> success(String message, T data) {
        return ResponseBuilder.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ResponseBuilder<T> success(String message) {
        return ResponseBuilder.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    public static <T> ResponseBuilder<T> error(String message) {
        return ResponseBuilder.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
} 