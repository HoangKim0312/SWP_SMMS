package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.StudentParent;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.StudentParentRepository;
import com.example.swp_smms.service.StudentParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import com.example.swp_smms.model.payload.response.StudentSummaryResponse;

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

    @Override
    public List<StudentSummaryResponse> getChildrenForParent(UUID parentId) {
        List<StudentParent> links = studentParentRepository.findByParent_AccountId(parentId);
        List<StudentSummaryResponse> children = new ArrayList<>();
        for (StudentParent link : links) {
            Account student = link.getStudent();
            StudentSummaryResponse summary = new StudentSummaryResponse();
            summary.setFullName(student.getFullName());
            summary.setClassName(student.getClazz() != null ? student.getClazz().getClassName() : null);
            children.add(summary);
        }
        return children;
    }
} 