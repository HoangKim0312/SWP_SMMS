package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.model.enums.RoleEnum;
import com.example.swp_smms.model.payload.request.ImportAccountRequest;
import com.example.swp_smms.model.payload.response.ImportAccountResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.AccountImportService;
import com.example.swp_smms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountImportServiceImpl implements AccountImportService {

    private static final Logger logger = LoggerFactory.getLogger(AccountImportServiceImpl.class);
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Random random = new Random();

    @Override
    @Transactional
    public ImportAccountResponse importAccounts(MultipartFile file) {
        List<ImportAccountResponse.ImportAccountResult> results = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Validate Excel structure
            validateExcelStructure(sheet);
            
            // Ensure all roles exist in database first
            ensureRolesExist();
            
            // List all available roles for debugging
            List<Role> allRoles = roleRepository.findAll();
            logger.info("Available roles in database: {}", 
                allRoles.stream().map(Role::getRoleName).collect(Collectors.joining(", ")));

            // Process all rows except header
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ImportAccountResponse.ImportAccountResult result = processRow(row);
                results.add(result);

                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failureCount++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage(), e);
        }

        return ImportAccountResponse.builder()
                .totalProcessed(results.size())
                .successCount(successCount)
                .failureCount(failureCount)
                .results(results)
                .build();
    }

    /**
     * Validates the Excel file structure
     */
    private void validateExcelStructure(Sheet sheet) {
        if (sheet.getLastRowNum() < 1) {
            throw new IllegalArgumentException("Excel file must have at least a header row and one data row");
        }

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Header row is missing");
        }

        // Check if we have at least 5 columns (email, firstName, lastName, phone, roleName)
        if (headerRow.getLastCellNum() < 5) {
            throw new IllegalArgumentException("Excel file must have at least 5 columns: Email, First Name, Last Name, Phone, Role Name");
        }

        logger.info("Excel structure validated. Columns: {}", headerRow.getLastCellNum());
    }

    /**
     * Ensures all roles from RoleEnum exist in the database
     */
    private void ensureRolesExist() {
        List<Role> existingRoles = roleRepository.findAll();
        List<String> existingRoleNames = existingRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (!existingRoleNames.contains(roleEnum.getRoleName())) {
                Role newRole = new Role();
                newRole.setRoleName(roleEnum.getRoleName());
                newRole.setDescription("Role for " + roleEnum.getRoleName());
                roleRepository.save(newRole);
                logger.info("Created new role: {}", roleEnum.getRoleName());
            }
        }
    }

    private ImportAccountResponse.ImportAccountResult processRow(Row row) {
        ImportAccountResponse.ImportAccountResult.ImportAccountResultBuilder resultBuilder = 
            ImportAccountResponse.ImportAccountResult.builder();

        try {
            String email = getCellValueAsString(row.getCell(0));
            String firstName = getCellValueAsString(row.getCell(1));
            String lastName = getCellValueAsString(row.getCell(2));
            String phone = getCellValueAsString(row.getCell(3));
            String roleName = getCellValueAsString(row.getCell(4));

            // Validate required fields
            if (email == null || firstName == null || lastName == null || roleName == null) {
                return resultBuilder
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .success(false)
                    .errorMessage("Missing required fields (email, firstName, lastName, roleName)")
                    .build();
            }

            // Check if email already exists
            if (accountRepository.existsByEmail(email)) {
                return resultBuilder
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .success(false)
                    .errorMessage("Email already exists")
                    .build();
            }

            // Find role by name (case-insensitive)
            logger.info("Looking for role: '{}'", roleName);
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseGet(() -> {
                        logger.info("Exact match not found, trying case-insensitive search for: '{}'", roleName);
                        return roleRepository.findByRoleNameIgnoreCase(roleName)
                                .orElse(null);
                    });

            if (role == null) {
                return resultBuilder
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .success(false)
                    .errorMessage("Invalid role: " + roleName + ". Available roles: " + 
                        roleRepository.findAll().stream().map(Role::getRoleName).collect(Collectors.joining(", ")))
                    .build();
            }

            // Generate random password
            String password = generateRandomPassword();

            // Create account
            Account account = new Account();
            account.setEmail(email);
            account.setUsername(email); // Using email as username
            account.setPassword(passwordEncoder.encode(password));
            account.setFullName(firstName + " " + lastName);
            account.setPhone(phone);
            account.setRole(role);

            account = accountRepository.save(account);

            // Send email with credentials
            emailService.sendAccountCredentials(email, firstName, password);

            return resultBuilder
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .accountId(account.getAccountId())
                .success(true)
                .build();

        } catch (Exception e) {
            return resultBuilder
                .email(getCellValueAsString(row.getCell(0)))
                .firstName(getCellValueAsString(row.getCell(1)))
                .lastName(getCellValueAsString(row.getCell(2)))
                .phone(getCellValueAsString(row.getCell(3)))
                .success(false)
                .errorMessage("Error processing row: " + e.getMessage())
                .build();
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
} 