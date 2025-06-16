package com.example.swp_smms.model.enums;

public enum RoleEnum {
    STUDENT("Student"),
    PARENT("Parent"),
    NURSE("Nurse"),
    MANAGER("Manager"),
    ADMIN("Admin");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static RoleEnum fromRoleName(String roleName) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.getRoleName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + roleName);
    }
} 