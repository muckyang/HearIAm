package com.ssafy.backend.controller;

import java.util.List;

import com.google.api.client.util.DateTime;
import com.ssafy.backend.dao.ReservationDao;
import com.ssafy.backend.dao.ScheduleDao;
import com.ssafy.backend.model.Schedule.ReserRequest;
import com.ssafy.backend.model.Schedule.Reservation;
import com.ssafy.backend.model.Schedule.Schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/api/schedule")
public class ScheduleController {
    @Autowired
    ScheduleDao scheduleDao;
    @Autowired
    ReservationDao reservationDao;

    @PostMapping("/create")
    public Object CreateSchedule(@RequestBody Schedule request) {
        Schedule Sche = new Schedule();
        Sche.setMentor(request.getMentor());
        Sche.setStime(request.getStime());
        scheduleDao.save(Sche);
        return 0;
    }

    @DeleteMapping("/delete")
    public Object DeleteSchedule(@RequestBody Schedule request) {
        Schedule Sche = scheduleDao.findScheduleByMentorAndSdateAndStime(request.getMentor(), request.getSdate(),
                request.getStime());
        scheduleDao.delete(Sche);
        return 0;
    }

    @PostMapping("/Reservation")
    public Object Reservation(@RequestBody ReserRequest request) {
        String mentee = request.getMentee();
        System.out.println(request.getStime());

        // 아직 매칭 안된 스케줄 중에 시간일치하는것 뽑아옴
        List<Schedule> list = scheduleDao.findScheduleBySdateAndStimeAndIsReser(request.getSdate(), request.getStime(),
                0);

        // 랜덤돌려서 매칭
        int number = (int) Math.random() * list.size();
        Schedule sche = list.get(number);
        sche.setIsReser(1);
        scheduleDao.save(sche);
        Reservation reser = new Reservation();
        reser.setScheNum(sche.getNum());
        reser.setMentee(mentee);
        reservationDao.save(reser);

        return 0;
    }
}
