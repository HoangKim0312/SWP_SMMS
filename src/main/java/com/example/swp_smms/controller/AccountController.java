package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.request.AccountUpdateRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.GetChildResponse;
import com.example.swp_smms.model.payload.response.PagedAccountResponse;
import com.example.swp_smms.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable UUID accountId,
            @RequestBody AccountUpdateRequest request) {
        AccountResponse response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<PagedAccountResponse> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(accountService.getAllAccounts(page, size));
    }

    @GetMapping("/search-sort-by-role")
    public ResponseEntity<PagedAccountResponse> getAccountsByRole(
            @RequestParam Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String name
    ) {
        Pageable pageable;

        if (sortBy == null || sortBy.trim().isEmpty()) {
            pageable = PageRequest.of(page, size); // No sorting
        } else {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            pageable = PageRequest.of(page, size, sort);
        }

        PagedAccountResponse response = accountService.getAccountsByRole(roleId, pageable, name);
        return ResponseEntity.ok(response);
    }

}
