package com.ssafy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ssafy.backend.model.AlarmReady;
@Repository
public interface AlarmReadyRepository extends JpaRepository<AlarmReady, Long>{
    // @Query("select count(*) from alarm_ready")
    public long count();
}
