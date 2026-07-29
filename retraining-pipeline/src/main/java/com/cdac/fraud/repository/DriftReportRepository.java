package com.cdac.fraud.repository;

import com.cdac.fraud.entity.DriftReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DriftReportRepository extends JpaRepository<DriftReport, Long> {
    
    // Spring Data JPA magically writes the SQL for this based on the method name!
    // It grabs the latest report based on the timestamp.
    Optional<DriftReport> findTopByOrderByGeneratedAtDesc();
}