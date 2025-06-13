package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountRequest {
    private String username;
    private String password;
    private String fullName;
    private String dob;
    private String gender;
    private String phone;
    private Long roleId;
}
