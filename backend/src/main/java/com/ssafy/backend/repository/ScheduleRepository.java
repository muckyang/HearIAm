package com.ssafy.backend.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ssafy.backend.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAll();

    Schedule findScheduleByMentorAndSdateAndStime(String Mentor, LocalDate sdate, LocalTime stime);

    List<Schedule> findScheduleBySdateAndStimeAndIsReser(LocalDate sdate, LocalTime stime, int flag);
}
