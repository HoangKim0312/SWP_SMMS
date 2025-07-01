package com.example.swp_smms.model.enums;

public enum MedicationType {
    MEDICATION("Thuốc"),
    MEDICAL_SUPPLY("Vật tư y tế");

    private final String displayName;

    MedicationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MedicationType fromDisplayName(String displayName) {
        for (MedicationType type : MedicationType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid medication type: " + displayName);
    }
} 