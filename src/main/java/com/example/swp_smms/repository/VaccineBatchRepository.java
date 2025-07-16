package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.VaccineBatch;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccineBatchRepository extends JpaRepository<VaccineBatch, Long> {
    List<VaccineBatch> findByVaccineId(Long vaccineId);

    @Query("SELECT vb FROM VaccineBatch vb WHERE vb.vaccineId = :vaccineId AND vb.expiryDate > CURRENT_DATE")
    List<VaccineBatch> findActiveByVaccineId(@Param("vaccineId") Long vaccineId);

}
