package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.Class;
import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.model.entity.StudentParent;
import com.example.swp_smms.model.enums.RoleEnum;
import com.example.swp_smms.model.payload.request.ImportAccountRequest;
import com.example.swp_smms.model.payload.response.ImportAccountResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.ClassRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.repository.StudentParentRepository;
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
    private final ClassRepository classRepository;
    private final RoleRepository roleRepository;
    private final StudentParentRepository studentParentRepository;
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

            // First pass: Create all accounts without linking
            List<Account> createdAccounts = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ImportAccountResponse.ImportAccountResult result = processRowWithoutLinking(row);
                results.add(result);

                if (result.isSuccess()) {
                    successCount++;
                    // Store created account for linking phase
                    Account createdAccount = accountRepository.findByEmail(result.getEmail()).orElse(null);
                    if (createdAccount != null) {
                        createdAccounts.add(createdAccount);
                    }
                } else {
                    failureCount++;
                }
            }

            // Second pass: Handle all linking
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String email = getCellValueAsString(row.getCell(0));
                String roleName = getCellValueAsString(row.getCell(4));
                String parentEmail = getCellValueAsString(row.getCell(5));
                String studentEmail = getCellValueAsString(row.getCell(6));
                String linkType = getCellValueAsString(row.getCell(7));
                String firstName = getCellValueAsString(row.getCell(1));
                String lastName = getCellValueAsString(row.getCell(2));

                Account account = accountRepository.findByEmail(email).orElse(null);
                if (account != null) {
                    String linkingMessage = handleParentStudentLinking(account, roleName, parentEmail, studentEmail, linkType, firstName, lastName);
                    
                    // Update the result with linking message
                    for (ImportAccountResponse.ImportAccountResult result : results) {
                        if (result.getEmail().equals(email)) {
                            result.setLinkingMessage(linkingMessage);
                            break;
                        }
                    }
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

        // Check if we have the optional linking columns (F, G, H, I, J, and K)
        boolean hasLinkingColumns = headerRow.getLastCellNum() >= 7;
        boolean hasPairMode = headerRow.getLastCellNum() >= 8;
        boolean hasClassName = headerRow.getLastCellNum() >= 9;
        boolean hasDob = headerRow.getLastCellNum() >= 10;
        boolean hasGender = headerRow.getLastCellNum() >= 11;
        logger.info("Excel structure validated. Columns: {}, Has linking columns: {}, Has PAIR mode: {}, Has class name: {}, Has DOB: {}, Has gender: {}", 
            headerRow.getLastCellNum(), hasLinkingColumns, hasPairMode, hasClassName, hasDob, hasGender);


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
        return processRowWithoutLinking(row);
    }

    private ImportAccountResponse.ImportAccountResult processRowWithoutLinking(Row row) {
        ImportAccountResponse.ImportAccountResult.ImportAccountResultBuilder resultBuilder = 
            ImportAccountResponse.ImportAccountResult.builder();

        try {
            String email = getCellValueAsString(row.getCell(0));
            String firstName = getCellValueAsString(row.getCell(1));
            String lastName = getCellValueAsString(row.getCell(2));
            String phone = getCellValueAsString(row.getCell(3));
            String roleName = getCellValueAsString(row.getCell(4));
            String parentEmail = getCellValueAsString(row.getCell(5)); // Column F - Parent Email
            String studentEmail = getCellValueAsString(row.getCell(6)); // Column G - Student Email
            String linkType = getCellValueAsString(row.getCell(7)); // Column H - Link Type
            String className = getCellValueAsString(row.getCell(8)); // Column I - Class Name
            String dob = getCellValueAsString(row.getCell(9)); // Column J - Date of Birth
            String gender = getCellValueAsString(row.getCell(10)); // Column K - Gender

            // Validate required fields
            if (email == null || firstName == null || lastName == null || roleName == null) {
                return resultBuilder
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .roleName(roleName)
                    .parentEmail(parentEmail)
                    .studentEmail(studentEmail)
                    .linkType(linkType)
                    .className(className)
                    .dob(normalizeDateFormat(dob))
                    .gender(gender)
                    .success(false)
                    .errorMessage("Missing required fields (email, firstName, lastName, roleName)")
                    .linkingMessage(null)
                    .build();
            }

            // Check if email already exists
            if (accountRepository.existsByEmail(email)) {
                return resultBuilder
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .roleName(roleName)
                    .parentEmail(parentEmail)
                    .studentEmail(studentEmail)
                    .linkType(linkType)
                    .className(className)
                    .dob(normalizeDateFormat(dob))
                    .gender(gender)
                    .success(false)
                    .errorMessage("Email already exists")
                    .linkingMessage(null)
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
                    .roleName(roleName)
                    .parentEmail(parentEmail)
                    .studentEmail(studentEmail)
                    .linkType(linkType)
                    .className(className)
                    .dob(normalizeDateFormat(dob))
                    .gender(gender)
                    .success(false)
                    .errorMessage("Invalid role: " + roleName + ". Available roles: " + 
                        roleRepository.findAll().stream().map(Role::getRoleName).collect(Collectors.joining(", ")))
                    .linkingMessage(null)
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
            
            // Handle gender
            if (gender != null && !gender.trim().isEmpty()) {
                account.setGender(gender.trim());
            }
            
            // Handle date of birth
            logger.info("Processing DOB for {}: '{}'", email, dob);
            if (dob != null && !dob.trim().isEmpty()) {
                String normalizedDob = normalizeDateFormat(dob);
                logger.info("Normalized DOB for {}: '{}' -> '{}'", email, dob, normalizedDob);
                if (normalizedDob != null) {
                    account.setDob(normalizedDob);
                    logger.info("Set DOB for {}: '{}'", email, normalizedDob);
                }
            } else {
                logger.info("DOB is null or empty for {}", email);
            }
            
            // Handle class assignment for students
            if (RoleEnum.STUDENT.getRoleName().equalsIgnoreCase(roleName) && className != null && !className.trim().isEmpty()) {
                Class clazz = findOrCreateClass(className);
                account.setClazz(clazz);
            }

            account = accountRepository.save(account);

            // Send email with credentials
            emailService.sendAccountCredentials(email, firstName, password);

            return resultBuilder
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .roleName(roleName)
                .parentEmail(parentEmail)
                .studentEmail(studentEmail)
                .linkType(linkType)
                .className(className)
                .dob(normalizeDateFormat(dob))
                .gender(gender)
                .accountId(account.getAccountId())
                .success(true)
                .linkingMessage("Account created successfully. Linking will be processed in second phase.")
                .build();

        } catch (Exception e) {
            return resultBuilder
                .email(getCellValueAsString(row.getCell(0)))
                .firstName(getCellValueAsString(row.getCell(1)))
                .lastName(getCellValueAsString(row.getCell(2)))
                .phone(getCellValueAsString(row.getCell(3)))
                .roleName(getCellValueAsString(row.getCell(4)))
                .parentEmail(getCellValueAsString(row.getCell(5)))
                .studentEmail(getCellValueAsString(row.getCell(6)))
                .linkType(getCellValueAsString(row.getCell(7)))
                .className(getCellValueAsString(row.getCell(8)))
                .dob(normalizeDateFormat(getCellValueAsString(row.getCell(9))))
                .gender(getCellValueAsString(row.getCell(10)))
                .success(false)
                .errorMessage("Error processing row: " + e.getMessage())
                .linkingMessage(null)
                .build();
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Check if it's a date
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    java.util.Date date = cell.getDateCellValue();
                    java.time.LocalDate localDate = date.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                    return localDate.format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
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

    /**
     * Handles parent-student linking based on the provided emails
     */
    private String handleParentStudentLinking(Account account, String roleName, String parentEmail, String studentEmail, String linkType, String firstName, String lastName) {
        try {
            // Check for PAIR mode first
            if ("PAIR".equalsIgnoreCase(linkType) && RoleEnum.STUDENT.getRoleName().equalsIgnoreCase(roleName) && parentEmail != null && !parentEmail.trim().isEmpty()) {
                // PAIR mode - create parent account and link
                return createParentAndLink(account, parentEmail, firstName, lastName);
            } else if (RoleEnum.STUDENT.getRoleName().equalsIgnoreCase(roleName) && parentEmail != null && !parentEmail.trim().isEmpty()) {
                // Student account - try to link with existing parent
                return linkStudentWithParent(account, parentEmail);
            } else if (RoleEnum.PARENT.getRoleName().equalsIgnoreCase(roleName) && studentEmail != null && !studentEmail.trim().isEmpty()) {
                // Parent account - try to link with existing student
                return linkParentWithStudent(account, studentEmail);
            }
            return null; // No linking attempted
        } catch (Exception e) {
            logger.warn("Error during parent-student linking for account {}: {}", account.getEmail(), e.getMessage());
            return "Linking failed: " + e.getMessage();
        }
    }

    /**
     * Links a student account with a parent account
     */
    private String linkStudentWithParent(Account studentAccount, String parentEmail) {
        Account parentAccount = accountRepository.findByEmail(parentEmail)
                .orElse(null);
        
        if (parentAccount == null) {
            return "Parent email not found: " + parentEmail;
        }

        // Check if parent has correct role
        if (!RoleEnum.PARENT.getRoleName().equalsIgnoreCase(parentAccount.getRole().getRoleName())) {
            return "Account with email " + parentEmail + " is not a parent";
        }

        // Check if link already exists
        if (studentParentRepository.existsByStudent_AccountIdAndParent_AccountId(studentAccount.getAccountId(), parentAccount.getAccountId())) {
            return "Link already exists between student and parent";
        }

        // Create the link
        StudentParent studentParent = new StudentParent();
        studentParent.setStudent(studentAccount);
        studentParent.setParent(parentAccount);
        studentParentRepository.save(studentParent);

        return "Successfully linked with parent: " + parentEmail;
    }

    /**
     * Links a parent account with a student account
     */
    private String linkParentWithStudent(Account parentAccount, String studentEmail) {
        Account studentAccount = accountRepository.findByEmail(studentEmail)
                .orElse(null);
        
        if (studentAccount == null) {
            return "Student email not found: " + studentEmail;
        }

        // Check if student has correct role
        if (!RoleEnum.STUDENT.getRoleName().equalsIgnoreCase(studentAccount.getRole().getRoleName())) {
            return "Account with email " + studentEmail + " is not a student";
        }

        // Check if link already exists
        if (studentParentRepository.existsByStudent_AccountIdAndParent_AccountId(studentAccount.getAccountId(), parentAccount.getAccountId())) {
            return "Link already exists between student and parent";
        }

        // Create the link
        StudentParent studentParent = new StudentParent();
        studentParent.setStudent(studentAccount);
        studentParent.setParent(parentAccount);
        studentParentRepository.save(studentParent);

        return "Successfully linked with student: " + studentEmail;
    }

    /**
     * Creates a parent account and links it with the student (PAIR mode)
     */
    private String createParentAndLink(Account studentAccount, String parentEmail, String studentFirstName, String studentLastName) {
        try {
            // Check if parent account already exists
            if (accountRepository.existsByEmail(parentEmail)) {
                // Parent exists, try to link with existing parent
                return linkStudentWithParent(studentAccount, parentEmail);
            }

            // Find parent role
            Role parentRole = roleRepository.findByRoleName(RoleEnum.PARENT.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Parent role not found"));

            // Generate random password for parent
            String parentPassword = generateRandomPassword();

            // Create parent account with default values
            Account parentAccount = new Account();
            parentAccount.setEmail(parentEmail);
            parentAccount.setUsername(parentEmail);
            parentAccount.setPassword(passwordEncoder.encode(parentPassword));
            parentAccount.setFullName("Parent of " + studentFirstName + " " + studentLastName);
            parentAccount.setPhone(null); // Empty phone, can be updated later
            parentAccount.setRole(parentRole);

            parentAccount = accountRepository.save(parentAccount);

            // Create the link
            StudentParent studentParent = new StudentParent();
            studentParent.setStudent(studentAccount);
            studentParent.setParent(parentAccount);
            studentParentRepository.save(studentParent);

            // Send email with credentials to parent
            emailService.sendAccountCredentials(parentEmail, "Parent of " + studentFirstName, parentPassword);

                    return "Successfully created parent account (" + parentEmail + ") and linked with student. Parent credentials sent via email.";
    } catch (Exception e) {
        logger.error("Error creating parent account for student {}: {}", studentAccount.getEmail(), e.getMessage());
        return "Failed to create parent account: " + e.getMessage();
    }
}

/**
 * Finds an existing class by name or creates a new one
 */
private Class findOrCreateClass(String className) {
    // Try to find existing class
    Class existingClass = classRepository.findByClassName(className).orElse(null);
    
    if (existingClass != null) {
        return existingClass;
    }
    
    // Create new class if it doesn't exist
    Class newClass = new Class();
    newClass.setClassName(className);
    newClass.setGrade(extractGradeFromClassName(className));
    newClass.setSchoolYear(getCurrentSchoolYear());
    newClass.setDescription("Class " + className);
    
    return classRepository.save(newClass);
}

/**
 * Extracts grade from class name (e.g., "10A" -> 10, "Grade 11B" -> 11)
 */
private int extractGradeFromClassName(String className) {
    try {
        // Remove non-numeric characters and extract first number
        String numericPart = className.replaceAll("[^0-9]", "");
        if (!numericPart.isEmpty()) {
            return Integer.parseInt(numericPart);
        }
    } catch (NumberFormatException e) {
        logger.warn("Could not extract grade from class name: {}", className);
    }
    return 1; // Default grade if extraction fails
}

/**
 * Gets the current school year
 */
private int getCurrentSchoolYear() {
    // Simple implementation - you might want to make this configurable
    return java.time.Year.now().getValue();
}

/**
 * Validates and normalizes date string to YYYY-MM-DD format
 */
private String normalizeDateFormat(String dateStr) {
    if (dateStr == null || dateStr.trim().isEmpty()) {
        return null;
    }
    
    try {
        // Try YYYY-MM-DD format first
        java.time.LocalDate.parse(dateStr);
        return dateStr;
    } catch (Exception e1) {
        try {
            // Try MM/DD/YYYY format (Excel default)
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("M/d/yyyy");
            java.time.LocalDate date = java.time.LocalDate.parse(dateStr, formatter);
            return date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e2) {
            try {
                // Try MM/DD/YY format
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("M/d/yy");
                java.time.LocalDate date = java.time.LocalDate.parse(dateStr, formatter);
                return date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e3) {
                logger.warn("Invalid date format: {}. Expected YYYY-MM-DD or MM/DD/YYYY format.", dateStr);
                return null;
            }
        }
    }
}
} 