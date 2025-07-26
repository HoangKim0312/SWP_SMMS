package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.VaccinationConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccinationConfirmationRepository extends JpaRepository<VaccinationConfirmation, Long> {
    List<VaccinationConfirmation> findByStudent_AccountId(UUID studentId);
    List<VaccinationConfirmation> findByParent_AccountId(UUID parentId);
    List<VaccinationConfirmation> findByVaccinationNotice_VaccineNoticeId(Long vaccineNoticeId);
    List<VaccinationConfirmation> findByStatus(String status);

    long countByVaccinationNotice_VaccineNoticeId(Long noticeId);


    @Modifying
    @Query("UPDATE VaccinationConfirmation vc SET vc.status = 'CONFIRMED', " +
            "vc.confirmedAt = :confirmedAt " +
            "WHERE vc.parent.accountId = :parentId AND vc.status = 'PENDING'")
    int confirmAllPendingByParent(@Param("parentId") UUID parentId,
                                  @Param("confirmedAt") String confirmedAt);


    @Query("SELECT vc.status, COUNT(vc) FROM VaccinationConfirmation vc " +
            "WHERE vc.vaccinationNotice.vaccineNoticeId = :noticeId " +
            "GROUP BY vc.status")
    List<Object[]> countByStatusForNotice(@Param("noticeId") Long noticeId);


    @Query("SELECT COUNT(vc) FROM VaccinationConfirmation vc WHERE vc.vaccinationNotice.vaccineNoticeId = :noticeId")
    long countByVaccinationNoticeId(@Param("noticeId") Long noticeId);


    // New method: get confirmations by student and status
    @Query("SELECT vc FROM VaccinationConfirmation vc " +
            "WHERE vc.status = :status AND vc.student.accountId IN (" +
            "SELECT sp.student.accountId FROM StudentParent sp WHERE sp.parent.accountId = :parentId)")
    List<VaccinationConfirmation> findByStatusAndParentLinkedStudents(@Param("status") String status,
                                                                      @Param("parentId") UUID parentId);


} 