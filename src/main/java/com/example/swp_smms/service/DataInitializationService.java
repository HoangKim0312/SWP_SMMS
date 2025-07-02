package com.example.swp_smms.service;

import java.util.List;

public interface DataInitializationService {
    void initializeRoles();
    List<String> getAvailableRoles();
} 