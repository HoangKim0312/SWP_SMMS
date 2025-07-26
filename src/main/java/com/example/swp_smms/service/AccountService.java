package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.request.AccountUpdateRequest;
import com.example.swp_smms.model.payload.request.ChangePasswordRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.PagedAccountResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    void changePassword(UUID accountId, ChangePasswordRequest request);
    List<ChildData> getAllChildrenByParentID(UUID parentAccountId);
    AccountResponse updateAccount(UUID accountId, AccountUpdateRequest request);
    PagedAccountResponse getAllAccounts(int page, int size, String name, Long roleId, String sortBy, String direction);
    PagedAccountResponse getAccountsByRole(Long roleId, Pageable pageable, String name);
    List<ChildData> getChildDataByClassId(Long classId);
    AccountResponse getAccountById(UUID accountId);
}
