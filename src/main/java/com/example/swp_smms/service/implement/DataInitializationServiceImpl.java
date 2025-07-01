package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.model.enums.RoleEnum;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.DataInitializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationServiceImpl implements DataInitializationService {

    private final RoleRepository roleRepository;

    @Override
    public void initializeRoles() {
        List<Role> existingRoles = roleRepository.findAll();
        List<String> existingRoleNames = existingRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        log.info("Existing roles in database: {}", existingRoleNames);
        log.info("Roles from RoleEnum: {}", 
            List.of(RoleEnum.values()).stream().map(RoleEnum::getRoleName).collect(Collectors.toList()));

        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (!existingRoleNames.contains(roleEnum.getRoleName())) {
                Role newRole = new Role();
                newRole.setRoleName(roleEnum.getRoleName());
                newRole.setDescription("Role for " + roleEnum.getRoleName());
                roleRepository.save(newRole);
                log.info("Created new role: {}", roleEnum.getRoleName());
            } else {
                log.info("Role already exists: {}", roleEnum.getRoleName());
            }
        }
        
        log.info("Role initialization completed. Total roles in database: {}", roleRepository.count());
    }

    @Override
    public List<String> getAvailableRoles() {
        return roleRepository.findAll().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }
} 