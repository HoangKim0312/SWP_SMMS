package com.example.swp_smms.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    
    public static ResponseEntity<Object> responseBuilder(HttpStatus httpStatus, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("http_status", httpStatus.value());
        response.put("time_stamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")));
        response.put("message", message);
        return ResponseEntity.status(httpStatus).body(response);
    }
    
    public static ResponseEntity<Object> responseBuilderWithData(HttpStatus httpStatus, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("http_status", httpStatus.value());
        response.put("time_stamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")));
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.status(httpStatus).body(response);
    }
} 