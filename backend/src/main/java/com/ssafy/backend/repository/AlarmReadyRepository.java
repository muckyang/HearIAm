package com.ssafy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;

import com.ssafy.backend.model.AlarmReady;
@Repository
public interface AlarmReadyRepository extends JpaRepository<AlarmReady, Long>{
    public long count();
    @Transactional
    @Modifying
    public void deleteByMentor(Long mentor);

}
