package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.request.ChangePasswordRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.GetChildResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    void changePassword(UUID accountId, ChangePasswordRequest request);
    List<ChildData> getAllChildrenByParentID(UUID parentAccountId);
}
