package com.example.swp_smms.model.enums;

public enum HealthEventApprovalStatus {
    PENDING("Pending", "Waiting for parent approval"),
    APPROVED("Approved", "Parent has approved the health event"),
    REJECTED("Rejected", "Parent has rejected the health event"),
    NOT_REQUIRED("Not Required", "No parent approval required for this event");

    private final String displayName;
    private final String description;

    HealthEventApprovalStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static HealthEventApprovalStatus fromDisplayName(String displayName) {
        for (HealthEventApprovalStatus status : HealthEventApprovalStatus.values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid approval status: " + displayName);
    }
} 