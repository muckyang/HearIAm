package com.ssafy.backend.repository;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.backend.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAll();

    Schedule findScheduleByMentorAndSdateAndStime(String Mentor, LocalDate sdate, String stime);

    List<Schedule> findScheduleBySdateAndStimeAndIsReser(LocalDate sdate, String stime, int flag);

    List<Schedule> findByMentor(String mentor);

    Schedule findScheduleByMentorAndSdateAndTimeidx(String Mentor, LocalDate sdate, int timeidx);
}
