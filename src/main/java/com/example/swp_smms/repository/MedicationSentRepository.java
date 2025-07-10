package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicationSent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface MedicationSentRepository extends JpaRepository<MedicationSent, Long> {

    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.student.accountId = :studentId " +
            "AND m.requestDate = :currentDate " +
            "AND m.isActive = true " +
            "AND (m.isAccepted IS NULL OR m.isAccepted = true)")
    List<MedicationSent> findActiveMedicationsByStudentIdAndDate(
            @Param("studentId") UUID studentId,
            @Param("currentDate") LocalDate currentDate);

    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.student.accountId = :studentId " +
            "AND m.isActive = true ")
    List<MedicationSent> findAllByStudentId(
            @Param("studentId") UUID studentId);

    @Modifying
    @Query("DELETE FROM MedicationSent m WHERE m.student.accountId = :studentId AND m.medSentId = :medicationSentId")
    void deleteByStudentIdAndMedicationSentId(@Param("studentId") UUID studentId,
                                              @Param("medicationSentId") Long medicationSentId);

    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.requestDate = :today " +
            "AND m.isActive = true " +
            "AND (m.isAccepted IS NULL OR m.isAccepted = true)")
    List<MedicationSent> findAllActiveMedications(@Param("today") LocalDate today);

    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.isActive = true " +
            "AND m.isAccepted = true " +
            "AND (:studentId IS NULL OR m.student.accountId = :studentId) " +
            "AND (:requestDate IS NULL OR m.requestDate = :requestDate)")
    List<MedicationSent> findAcceptedWithOptionalFilters(
            @Param("studentId") UUID studentId,
            @Param("requestDate") LocalDate requestDate);

    @Query("SELECT m FROM MedicationSent m " +
            "WHERE m.isActive = true " +
            "AND m.isAccepted = false " +
            "AND (:studentId IS NULL OR m.student.accountId = :studentId) " +
            "AND (:requestDate IS NULL OR m.requestDate = :requestDate)")
    List<MedicationSent> findDeclinedWithOptionalFilters(@Param("studentId") UUID studentId,
                                                         @Param("requestDate") LocalDate requestDate);



}
