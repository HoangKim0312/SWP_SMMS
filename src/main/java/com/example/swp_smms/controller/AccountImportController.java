package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.response.ImportAccountResponse;
import com.example.swp_smms.service.AccountImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountImportController {

    private final AccountImportService accountImportService;

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImportAccountResponse> importAccounts(
            @RequestParam("file") MultipartFile file) {
        ImportAccountResponse response = accountImportService.importAccounts(file);
        return ResponseEntity.ok(response);
    }
} 