package com.example.swp_smms.model.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportAccountRequest {
    private MultipartFile file;
    private String role; // Role name from RoleEnum
} 