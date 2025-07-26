package com.example.swp_smms.model.enums;

public enum HealthEventPriority {
    LOW("Low", "Can be handled at school, no parent notification needed"),
    MEDIUM("Medium", "Requires parent notification via email, approval needed"),
    HIGH("High", "Requires immediate parent notification via email, urgent approval needed"),
    CRITICAL("Critical", "Emergency notification, immediate action required");

    private final String displayName;
    private final String description;

    HealthEventPriority(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean requiresParentNotification() {
        return this == MEDIUM || this == HIGH || this == CRITICAL;
    }

    public boolean requiresParentApproval() {
        return this == MEDIUM || this == HIGH || this == CRITICAL;
    }

    public boolean isEmergency() {
        return this == CRITICAL;
    }

    public static HealthEventPriority fromDisplayName(String displayName) {
        for (HealthEventPriority priority : HealthEventPriority.values()) {
            if (priority.getDisplayName().equalsIgnoreCase(displayName)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid priority: " + displayName);
    }
} 