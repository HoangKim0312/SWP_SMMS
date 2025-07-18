package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.MedicalProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalProfileRepository extends JpaRepository<MedicalProfile, Long> {
    boolean existsByStudent_AccountId(UUID accountId);
    Optional<MedicalProfile> findByStudent(Account student);

    @Query("SELECT m FROM MedicalProfile m WHERE m.student.accountId = :studentId ORDER BY m.lastUpdated DESC")
    List<MedicalProfile> findMedicalProfilesByStudentId(@Param("studentId") UUID studentId, Pageable pageable);

    @Query("SELECT m FROM MedicalProfile m WHERE m.student.accountId = :studentId ORDER BY m.lastUpdated DESC")
    List<MedicalProfile> findMedicalProfilesByStudentId(@Param("studentId") UUID studentId);

    @Modifying
    @Query("DELETE FROM MedicalProfile m WHERE m.student.accountId = :studentId AND m.medicalProfileId = :medicalProfileId")
    void deleteByStudentIdAndMedicalProfileId(@Param("studentId") UUID studentId,
                                              @Param("medicalProfileId") Long medicalProfileId);

}
