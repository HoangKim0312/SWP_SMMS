package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
