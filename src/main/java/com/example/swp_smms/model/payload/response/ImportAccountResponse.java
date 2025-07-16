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
        private String roleName;
        private String parentEmail;
        private String studentEmail;
        private String linkType;
        private String className;
        private String dob;
        private String gender;
        private UUID accountId;
        private boolean success;
        private String errorMessage;
        private String linkingMessage;
        
        // Add setter for linkingMessage since we need to update it after creation
        public void setLinkingMessage(String linkingMessage) {
            this.linkingMessage = linkingMessage;
        }
    }
} 