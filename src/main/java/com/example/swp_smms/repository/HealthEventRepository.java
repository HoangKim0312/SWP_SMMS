package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HealthEventRepository extends JpaRepository<HealthEvent, Long> {
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM HealthEvent h WHERE h.student.accountId = :#{#healthEvent.student.accountId} AND h.eventDate = :#{#healthEvent.eventDate} AND h.eventType = :#{#healthEvent.eventType}")
    boolean isExisted(@Param("healthEvent") HealthEvent healthEvent);
   
}
