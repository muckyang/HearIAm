package com.ssafy.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.backend.model.ConRoom;

@Repository
public interface ConRoomRepository extends JpaRepository<ConRoom, Long> {
    List<ConRoom> findByStatus(String status);
    List<ConRoom> findByMentee(Long mentee);
    List<ConRoom> findByMenteeOrderByNumDesc(Long mentee);
    List<ConRoom> findByMenteeOrderByDateDesc(Long mentee);
    List<ConRoom> findByMentor(Long mentor);
    List<ConRoom> findByMentorAndMenteeAndStatusOrderByDateDesc(Long mentor, Long mentee, String status);
    ConRoom findByRoom(String room);
    ConRoom findByNum(Long num);
    ConRoom findByMentorAndDate(Long mentor, LocalDateTime date);
    @Transactional
    @Modifying
    void deleteByNum(Long num);
    @Transactional
    @Modifying
    void deleteByMentorAndDate(Long num, LocalDateTime date);
    List<ConRoom> findByMentorAndStatus(Long mentor, String status);
    List<ConRoom> findByMentorAndStatusOrderByDateAsc(Long mentor, String status);
    List<ConRoom> findByMentorAndStatusOrStatusOrderByDateAsc(Long mentor, String status1,String status2);
    List<ConRoom> findByStatusOrderByDateDesc(String status, Pageable page);
    @Query("select r from ConRoom r where r.date > :time AND r.mentor = :num AND (r.status = 'reapply' OR r.status = 'reserve' ) order by date asc")
    List<ConRoom> findByAfterReservation(@Param("num") Long num, @Param("time") LocalDateTime time);
    
    @Query("select r from ConRoom r where r.date > :time AND r.mentee = :num AND (r.status = 'reapply' OR r.status = 'reserve' ) order by date asc")
    List<ConRoom> findByAfterMenteeReservation(@Param("num") Long num, @Param("time") LocalDateTime time);



}
