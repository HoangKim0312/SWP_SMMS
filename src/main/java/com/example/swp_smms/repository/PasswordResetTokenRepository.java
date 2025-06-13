package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    Optional<PasswordResetToken> findByToken(String token);
    
    List<PasswordResetToken> findAllByExpiredBefore(LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM PasswordResetToken prt WHERE prt.revoked = true")
    void deleteAllByRevoked(boolean revoked);
} 