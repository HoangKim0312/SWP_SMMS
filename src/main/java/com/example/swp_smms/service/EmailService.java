package com.example.swp_smms.service;

public interface EmailService {
    void sendAccountCredentials(String email, String firstName, String password);
} 