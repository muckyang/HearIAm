package com.ssafy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.backend.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAll();

    Schedule findScheduleByMentorAndSdateAndStime(String Mentor, String sdate, String stime);
    
    Schedule findBySdateAndStime(String sdate, String stime);
    
    Schedule findByNum(Long num);

    List<Schedule> findScheduleBySdateAndStimeAndIsReser(String sdate, String stime, int flag);

    List<Schedule> findByMentor(String mentor);
    
    List<Schedule> findByMentorAndIsReser(String mentor, int isReser);

    Schedule findScheduleByMentorAndSdateAndTimeidx(String Mentor, String sdate, int timeidx);

    List<Schedule> findBySdateAndIsReser(String sdate, int isReser);
    
    List<Schedule> findBySdateAndIsReserOrderByTimeidxAsc(String sdate, int isReser);

    List<Schedule> findByMentorAndSdateAndIsReserOrderByTimeidxAsc(String mentor, String sdate, int isReser);
}
