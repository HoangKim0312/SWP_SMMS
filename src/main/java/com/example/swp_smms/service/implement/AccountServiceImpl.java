package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.GetChildResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.swp_smms.model.payload.request.ChangePasswordRequest;
import com.example.swp_smms.exception.SmmsException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        // Check if the username already exists
        if (accountRepository.findByUsername(accountRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Role role = roleRepository.findById(accountRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Account account = modelMapper.map(accountRequest, Account.class);

        account.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        account.setRole(role);

        Account savedAccount = accountRepository.save(account);

        return modelMapper.map(savedAccount, AccountResponse.class);
    }

    @Override
    public void changePassword(UUID accountId, ChangePasswordRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new SmmsException(HttpStatus.NOT_FOUND, "Account not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        // Verify new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "New password and confirm password do not match");
        }

        // Verify new password is different from current password
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "New password must be different from current password");
        }

        // Update password
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Override
    public List<ChildData> getAllChildrenByParentID(UUID parentAccountId) {
        return accountRepository.findChildrenAccounts(parentAccountId);
    }

}
