package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class UpdateStudentLinksActiveRequest {
    private Long Id;
    private boolean active;
}
