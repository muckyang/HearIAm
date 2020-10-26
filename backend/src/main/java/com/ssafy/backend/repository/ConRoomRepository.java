package com.ssafy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.ssafy.backend.model.ConRoom;

@Repository
public interface ConRoomRepository extends JpaRepository<ConRoom, Long> {
    List<ConRoom> findByStatus(String status);
    List<ConRoom> findByMentee(Long mentee);
    List<ConRoom> findByMentor(Long mentor);
    List<ConRoom> findByMentorAndMentee(Long mentor, Long mentee);
    ConRoom findByNum(Long num);
    @Transactional
    @Modifying
	void deleteByNum(Long num);
}
