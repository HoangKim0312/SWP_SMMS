package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.StudentParent;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.StudentParentRepository;
import com.example.swp_smms.service.StudentParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentParentServiceImpl implements StudentParentService {
    private final StudentParentRepository studentParentRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void linkStudentParent(UUID studentId, UUID parentId) {
        if (studentParentRepository.existsByStudent_AccountIdAndParent_AccountId(studentId, parentId)) {
            throw new IllegalArgumentException("Link already exists");
        }
        Account student = accountRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Account parent = accountRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent not found"));
        StudentParent link = new StudentParent();
        link.setStudent(student);
        link.setParent(parent);
        studentParentRepository.save(link);
    }

    @Override
    @Transactional
    public void unlinkStudentParent(UUID studentId, UUID parentId) {
        studentParentRepository.deleteByStudent_AccountIdAndParent_AccountId(studentId, parentId);
    }
} 