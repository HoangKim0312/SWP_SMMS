package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {
    boolean existsByStudent_AccountIdAndParent_AccountId(java.util.UUID studentId, java.util.UUID parentId);
    void deleteByStudent_AccountIdAndParent_AccountId(java.util.UUID studentId, java.util.UUID parentId);
} 