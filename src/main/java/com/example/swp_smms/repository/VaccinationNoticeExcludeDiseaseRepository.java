package com.example.swp_smms.repository;
import com.example.swp_smms.model.entity.VaccineNoticeExcludeDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationNoticeExcludeDiseaseRepository extends JpaRepository<VaccineNoticeExcludeDisease, Long> {
}
