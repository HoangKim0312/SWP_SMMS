package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.response.ImportAccountResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AccountImportService {
    ImportAccountResponse importAccounts(MultipartFile file);
} 