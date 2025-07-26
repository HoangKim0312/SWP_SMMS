package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findAll();
    List<Disease> findByNameContainingIgnoreCase(String name);

}