package com.ssafy.backend.repository;

import com.ssafy.backend.model.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    List<Reservation> findAll();
    List<Reservation> findByMentee(String mentee);
    Boolean existsByMentee(String mentee);
    @Transactional
    @Modifying
	void deleteByScheNum(Long num);
}
