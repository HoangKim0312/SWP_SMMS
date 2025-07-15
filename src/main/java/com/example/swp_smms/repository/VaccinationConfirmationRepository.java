package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.VaccinationConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.VaccinationConfirmationRepository;
import com.example.swp_smms.model.entity.VaccinationConfirmation;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccinationConfirmationRepository extends JpaRepository<VaccinationConfirmation, Long> {
    List<VaccinationConfirmation> findByStudent_AccountId(UUID studentId);
    List<VaccinationConfirmation> findByParent_AccountId(UUID parentId);
    List<VaccinationConfirmation> findByVaccinationNotice_VaccineNoticeId(Long vaccineNoticeId);
    List<VaccinationConfirmation> findByStatus(String status);
    List<VaccinationConfirmation> findByStatusAndParent_AccountId(String status, UUID parentId);

    @Modifying
    @Query("UPDATE VaccinationConfirmation vc SET vc.status = 'CONFIRMED', " +
            "vc.confirmedAt = :confirmedAt " +
            "WHERE vc.parent.accountId = :parentId AND vc.status = 'PENDING'")
    int confirmAllPendingByParent(@Param("parentId") UUID parentId,
                                  @Param("confirmedAt") String confirmedAt);


} 