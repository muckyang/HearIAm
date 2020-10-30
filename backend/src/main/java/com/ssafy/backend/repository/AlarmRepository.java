package com.ssafy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ssafy.backend.model.Alarm;
@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{
    
}
