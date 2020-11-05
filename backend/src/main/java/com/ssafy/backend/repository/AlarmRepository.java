package com.ssafy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

import com.ssafy.backend.model.Alarm;
@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{
    // public Optional<Alarm> findByRoom(String room);
    // @Transactional
    // @Modifying
    // public void deleteByRoom(String room);
    public long count();
    @Transactional
    @Modifying
    void deleteByNum(Long num);
    public List<Alarm> findByMentor(Long i);
    

}
