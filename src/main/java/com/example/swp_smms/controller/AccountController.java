package com.example.swp_smms.controller;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.GetChildResponse;
import com.example.swp_smms.service.AccountService;
import com.example.swp_smms.model.payload.request.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@RequestBody AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest);
    }

    @PutMapping("/{accountId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID accountId,
            @Valid @RequestBody ChangePasswordRequest request) {
        accountService.changePassword(accountId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<GetChildResponse> getChildrenByParentId(@PathVariable UUID parentId) {
        List<ChildData> children = accountService.getAllChildrenByParentID(parentId);
        GetChildResponse response = new GetChildResponse();
        response.setChildren(children);
        return ResponseEntity.ok(response);
    }


}
