package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.LoginRequest;
import com.example.swp_smms.model.payload.response.AuthResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.NoSuchAlgorithmException;

public interface AuthenticationService {
    AuthResponse authenticate(LoginRequest request);
    
    void logout(HttpServletRequest request);
    
    AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
    
    void forgotPassword(String email) throws NoSuchAlgorithmException, MessagingException;
    
    void resetPassword(String password, String token);
} 