package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Long> {
    boolean existsByClassName(String className);
}
