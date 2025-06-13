package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
}
