package com.example.swp_smms.repository;


import com.example.swp_smms.model.entity.SyndromeDisability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyndromeDisabilityRepository extends JpaRepository<SyndromeDisability, Long> {
    List<SyndromeDisability> findAll();
    List<SyndromeDisability> findByNameContainingIgnoreCase(String name);
}