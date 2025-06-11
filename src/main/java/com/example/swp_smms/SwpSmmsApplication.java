package com.example.swp_smms;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class SwpSmmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwpSmmsApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initAdminAccount(AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String plaintextPassword = "admin123"; // 👈 This is the original password
            System.out.println("Admin password before encryption: " + plaintextPassword);

            // Check if an admin account already exists to avoid duplicate
            if (accountRepository.findByUsername("admin").isEmpty()) {
                // Ensure the "ADMIN" role exists
                Role adminRole = roleRepository.findByRoleName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Admin role not found"));

                // Create the admin account
                Account admin = new Account();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode(plaintextPassword)); // Encrypt the password
                admin.setFullName("System Administrator");
                admin.setRole(adminRole);
                admin.setAccountId(UUID.randomUUID());

                // Save the admin account to the database
                accountRepository.save(admin);
                System.out.println("✅ Admin account created with username: admin");
            }
        };
    }
}
