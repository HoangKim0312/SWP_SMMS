package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicationSent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicationSentRepository extends JpaRepository<MedicationSent, Long> {
    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.student.accountId = :studentId " +
            "AND :currentDate = m.sentAt")
    List<MedicationSent> findActiveMedicationsByStudentIdAndDate(
            @Param("studentId") UUID studentId,
            @Param("currentDate") String currentDate);


    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.student.accountId = :studentId ")
    List<MedicationSent> findAllByStudentId(
            @Param("studentId") UUID studentId);

    @Modifying
    @Query("DELETE FROM MedicationSent m WHERE m.student.accountId = :studentId AND m.medSentId = :medicationSentId")
    void deleteByStudentIdAndMedicationSentId(@Param("studentId") UUID studentId,
                                              @Param("medicationSentId") Long medicationSentId);

    @Query("SELECT m FROM MedicationSent m WHERE m.sentAt = :today")
    List<MedicationSent> findAllActiveMedications(@Param("today") String today);


}
