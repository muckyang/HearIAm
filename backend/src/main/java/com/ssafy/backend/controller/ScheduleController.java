package com.ssafy.backend.controller;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.backend.model.Schedule;
import com.ssafy.backend.repository.ScheduleRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    ScheduleRepository scheduleRepository;

    @PostMapping("/saveTime/{mentor}")
    public Object saveTime(@RequestBody Map<Object, int[]> timetable, @PathVariable String mentor) {
        String newDate = "";
        String newTime = "";
        String[] timeArr = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00" };
        LocalDate today = LocalDate.now();
        int todayWeek = DayOfWeek.from(LocalDate.now()).getValue(); // 일 : 0
        for (int i = 0; i <= 6; i++) {
            if (i < todayWeek) { // 오늘보다 이전이면
                newDate = today.minusDays(todayWeek - i).toString();
            } else if (i > todayWeek) {
                newDate = today.plusDays(i - todayWeek).toString();
            } else {
                newDate = today.toString();
            }
            for (int t : timetable.get(String.valueOf(i))) {
                newTime = timeArr[t];
                Schedule schedule = new Schedule();
                schedule.setMentor(mentor);
                System.out.println(newTime + " " + LocalTime.parse(newTime));
                schedule.setSdate(LocalDate.parse(newDate));
                schedule.setStime(newTime);
                scheduleRepository.save(schedule);
            }
        }

        return timetable;
    }
}
