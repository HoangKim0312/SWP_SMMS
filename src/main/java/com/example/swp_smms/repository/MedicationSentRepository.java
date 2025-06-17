package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicationSent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicationSentRepository extends JpaRepository<MedicationSent, Long> {
    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.student.accountId = :studentId " +
            "AND :currentDate >= m.startDate " +
            "AND :currentDate <= m.endDate")
    List<MedicationSent> findActiveMedicationsByStudentIdAndDate(
            @Param("studentId") UUID studentId,
            @Param("currentDate") String currentDate);

}
