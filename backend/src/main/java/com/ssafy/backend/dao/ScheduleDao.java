package com.ssafy.backend.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.ssafy.backend.model.Schedule.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDao extends JpaRepository<Schedule, String> {
    List<Schedule> findAll();

    Schedule findScheduleByMentorAndSdateAndStime(String Mentor, LocalDate sdate, String stime);

    List<Schedule> findScheduleBySdateAndStimeAndIsReser(LocalDate sdate, String stime, int flag);
}