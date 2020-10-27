package com.ssafy.backend.repository;

import com.ssafy.backend.model.ConReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConReportRepository extends JpaRepository<ConReport,Long>{
    
}
