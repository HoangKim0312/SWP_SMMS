package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.GetChildResponse;
import com.example.swp_smms.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.ok(accountService.createAccount(accountRequest));
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<GetChildResponse> getChildrenByParentId(@PathVariable UUID parentId) {
        List<ChildData> children = accountService.getAllChildrenByParentID(parentId);
        GetChildResponse response = new GetChildResponse();
        response.setChildren(children);
        return ResponseEntity.ok(response);
    }


}
