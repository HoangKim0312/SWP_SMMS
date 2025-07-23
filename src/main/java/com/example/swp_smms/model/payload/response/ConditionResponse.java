package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionResponse {
    private Long conditionId;
    private String name;
    private String type;
    private int priority;
}
