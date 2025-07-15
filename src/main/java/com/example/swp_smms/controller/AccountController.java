package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.request.AccountUpdateRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.GetChildResponse;
import com.example.swp_smms.model.payload.response.PagedAccountResponse;
import com.example.swp_smms.service.AccountService;
import com.example.swp_smms.service.DataInitializationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final DataInitializationService dataInitializationService;

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
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long roleId,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(accountService.getAllAccounts(page, size, name, roleId, sortBy, direction));
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

    @PostMapping("/initialize-roles")
    public ResponseEntity<String> initializeRoles() {
        try {
            dataInitializationService.initializeRoles();
            return ResponseEntity.ok("Roles initialized successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error initializing roles: " + e.getMessage());
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAvailableRoles() {
        try {
            List<String> roles = dataInitializationService.getAvailableRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Error getting roles: " + e.getMessage()));
        }
    }

    @GetMapping("/students/child-data/by-class")
    public ResponseEntity<List<ChildData>> getChildDataByClass(@RequestParam Long classId) {
        List<ChildData> students = accountService.getChildDataByClassId(classId);
        return ResponseEntity.ok(students);
    }


}
