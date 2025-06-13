package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {
    AccessToken findByToken(String token);
    
    @Query("SELECT at FROM AccessToken at WHERE at.account.accountId = :accountId AND at.revoked = false AND at.expired = false")
    List<AccessToken> findAllValidTokensByUser(UUID accountId);
} 