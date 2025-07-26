package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.model.payload.request.AccountFilterRequest;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.request.AccountUpdateRequest;
import com.example.swp_smms.model.payload.response.*;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.swp_smms.model.payload.request.ChangePasswordRequest;
import com.example.swp_smms.model.exception.SmmsException;
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
    @Override
    public AccountResponse updateAccount(UUID accountId, AccountUpdateRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (isNotBlank(request.getFullName())) {
            account.setFullName(request.getFullName());
        }
        if (isNotBlank(request.getDob())) {
            account.setDob(request.getDob());
        }
        if (isNotBlank(request.getGender())) {
            account.setGender(request.getGender());
        }
        if (isNotBlank(request.getPhone())) {
            account.setPhone(request.getPhone());
        }
        if (request.getEmailNotificationsEnabled() != null) {
            account.setEmailNotificationsEnabled(request.getEmailNotificationsEnabled());
        }
        if (request.getNotificationTypes() != null) {
            account.setNotificationTypes(request.getNotificationTypes());
        }

        Account updated = accountRepository.save(account);
        return modelMapper.map(updated, AccountResponse.class);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }


    @Override
    public PagedAccountResponse getAllAccounts(int page, int size, String name, Long roleId, String sortBy, String direction) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size,
                org.springframework.data.domain.Sort.by(
                        "desc".equalsIgnoreCase(direction) ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC,
                        sortBy
                )
        );

        Page<Account> accountPage;
        if (roleId != null && name != null && !name.trim().isEmpty()) {
            accountPage = accountRepository.findByRole_RoleIdAndFullNameContainingIgnoreCase(roleId, name, pageable);
        } else if (roleId != null) {
            accountPage = accountRepository.findByRole_RoleId(roleId, pageable);
        } else if (name != null && !name.trim().isEmpty()) {
            accountPage = accountRepository.findByFullNameContainingIgnoreCase(name, pageable);
        } else {
            accountPage = accountRepository.findAll(pageable);
        }

        List<AccountResponse> responses = accountPage.getContent().stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .toList();

        PagedAccountResponse pagedResponse = new PagedAccountResponse();
        pagedResponse.setAccounts(responses);
        pagedResponse.setCurrentPage(accountPage.getNumber());
        pagedResponse.setTotalPages(accountPage.getTotalPages());
        pagedResponse.setTotalItems(accountPage.getTotalElements());

        return pagedResponse;
    }
    @Override
    public PagedAccountResponse getAccountsByRole(Long roleId, Pageable pageable, String name) {
        Page<Account> page;

        if (name == null || name.trim().isEmpty()) {
            page = accountRepository.findByRole_RoleId(roleId, pageable);
        } else {
            page = accountRepository.findByRole_RoleIdAndFullNameContainingIgnoreCase(roleId, name, pageable);
        }

        List<AccountResponse> accountResponses = page.getContent().stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .toList();

        PagedAccountResponse response = new PagedAccountResponse();
        response.setAccounts(accountResponses);
        response.setCurrentPage(page.getNumber());
        response.setTotalPages(page.getTotalPages());
        response.setTotalItems(page.getTotalElements());

        return response;
    }

    @Override

    public List<ChildData> getChildDataByClassId(Long classId) {
        return accountRepository.findChildDataByClassId(classId);
    }

    @Override
    public StudentMedicalSummaryResponse getStudentMedicalSummary(UUID studentId) {
        Account student = accountRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MedicalProfile profile = student.getMedicalProfile();
        if (profile == null || profile.getBasicHealthData() == null) {
            throw new RuntimeException("Medical profile or basic health data not found");
        }

        StudentMedicalSummaryResponse response = new StudentMedicalSummaryResponse();
        response.setClassId(student.getClazz().getClassId());
        response.setClassName(student.getClazz().getClassName());
        response.setGrade(student.getClazz().getGrade());
        response.setFullName(student.getFullName());
        response.setDob(student.getDob());
        response.setGender(profile.getBasicHealthData().getGender());
        return response;
    }


    @Override
    public Page<AccountResponse> filterAccounts(AccountFilterRequest request) {
        return accountRepository.filterStudents(request);
    }

}
