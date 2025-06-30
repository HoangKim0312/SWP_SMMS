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
}
