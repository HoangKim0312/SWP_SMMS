package com.example.swp_smms.model.payload.response;

import com.example.swp_smms.model.entity.Account;
import lombok.Data;

import java.util.List;

@Data
public class GetChildResponse {
    private List<ChildData> children;
}
