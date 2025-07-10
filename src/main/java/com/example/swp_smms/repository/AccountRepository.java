package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.response.ChildData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
    boolean existsByAccountIdAndRole_RoleId(UUID accountId, Long roleId);
    boolean existsByEmail(String email);
    Page<Account> findByRole_RoleId(Long roleId, Pageable pageable);
    Page<Account> findByRole_RoleIdAndFullNameContainingIgnoreCase(Long roleId, String name, Pageable pageable);
    Page<Account> findByFullNameContainingIgnoreCase(String name, Pageable pageable);

    Account findAccountByAccountId(UUID accountId);

    @Query("SELECT new com.example.swp_smms.model.payload.response.ChildData(sp.student.accountId, sp.student.fullName, sp.student.clazz.classId) " +
            "FROM StudentParent sp WHERE sp.parent.accountId = :parentAccountId")
    List<ChildData> findChildrenAccounts(@Param("parentAccountId") UUID parentAccountId);

    @Query("SELECT DISTINCT new com.example.swp_smms.model.payload.response.ChildData(a.accountId, a.fullName, a.clazz.classId) " +
            "FROM Account a JOIN a.medicationSents ms " +
            "WHERE ms.requestDate = :today AND ms.isActive = true")
    List<ChildData> findStudentsWithOngoingMedication(@Param("today") LocalDate today);


}
