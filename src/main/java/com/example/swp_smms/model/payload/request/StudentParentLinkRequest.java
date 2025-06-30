package com.example.swp_smms.model.payload.request;

import lombok.Data;
import java.util.UUID;

@Data
public class StudentParentLinkRequest {
    private UUID studentId;
    private UUID parentId;
} 