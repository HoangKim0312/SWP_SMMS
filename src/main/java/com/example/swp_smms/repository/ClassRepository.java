package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Long> {
    boolean existsByClassName(String className);
    
    java.util.Optional<Class> findByClassName(String className);

    @Query("SELECT c FROM Class c WHERE c.grade = :grade AND c.schoolYear = :currentYear")
    List<Class> findByGradeAndCurrentYear(@Param("grade") String grade, @Param("currentYear") int currentYear);


}
