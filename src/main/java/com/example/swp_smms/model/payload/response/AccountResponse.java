package com.example.swp_smms.model.payload.response;

import lombok.Data;
import java.util.UUID;

@Data
public class AccountResponse {
    private UUID accountId;
    private String username;
    private String fullName;
    private String dob;
    private String gender;
    private String phone;
    private Long roleId;
    private String email;
    private Boolean emailNotificationsEnabled;
    private String notificationTypes;

    public AccountResponse(UUID accountId, String username, String fullName, String dob, String gender,
                           String phone, Long roleId, String email,
                           Boolean emailNotificationsEnabled, String notificationTypes) {
        this.accountId = accountId;
        this.username = username;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.roleId = roleId;
        this.email = email;
        this.emailNotificationsEnabled = emailNotificationsEnabled;
        this.notificationTypes = notificationTypes;
    }

    public AccountResponse() {}
}