// AccountUpdateRequest.java
package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class AccountUpdateRequest {
    private String fullName;
    private String dob;
    private String gender;
    private String phone;
}
