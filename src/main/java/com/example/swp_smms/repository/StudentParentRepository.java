package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {
    boolean existsByStudent_AccountIdAndParent_AccountId(java.util.UUID studentId, java.util.UUID parentId);
    void deleteByStudent_AccountIdAndParent_AccountId(java.util.UUID studentId, java.util.UUID parentId);
    List<StudentParent> findByParent_AccountId(UUID parentId);
} 