package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class ChildData {
    private UUID childId;
    private String fullName;
}
