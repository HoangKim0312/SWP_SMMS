package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ImportAccountResponse {
    private int totalProcessed;
    private int successCount;
    private int failureCount;
    private List<ImportAccountResult> results;

    @Data
    @Builder
    public static class ImportAccountResult {
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private UUID accountId;
        private boolean success;
        private String errorMessage;
    }
} 