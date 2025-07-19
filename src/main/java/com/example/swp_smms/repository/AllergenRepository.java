package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, Long> {
    List<Allergen> findByNameContainingIgnoreCase(String name);
}