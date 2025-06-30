package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface HealthEventRepository extends JpaRepository<HealthEvent, Long> {
    boolean existsHealthEventByEventId(Long eventId);
    ArrayList<HealthEvent> getHealthEventByEventId(Long eventId);
}
