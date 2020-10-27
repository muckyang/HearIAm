package com.ssafy.backend.dao;

import com.ssafy.backend.model.Schedule.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, String> {
    List<Reservation> findAll();
}
