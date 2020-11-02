package com.ssafy.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.backend.model.ConRoom;

@Repository
public interface ConRoomRepository extends JpaRepository<ConRoom, Long> {
    List<ConRoom> findByStatus(String status);
    List<ConRoom> findByMentee(Long mentee);
    List<ConRoom> findByMenteeOrderByNumDesc(Long mentee);
    List<ConRoom> findByMentor(Long mentor);
    List<ConRoom> findByMentorAndMentee(Long mentor, Long mentee);
    ConRoom findByRoom(String room);
    ConRoom findByNum(Long num);
    ConRoom findByMentorAndDate(Long mentor, LocalDateTime date);
    @Transactional
    @Modifying
    void deleteByNum(Long num);
    List<ConRoom> findByMentorAndStatus(Long mentor, String status);
    List<ConRoom> findByMentorAndStatusOrStatusOrderByDateAsc(Long mentor, String status1,String status2);
    List<ConRoom> findByStatusOrderByDateDesc(String status, Pageable page);
}
