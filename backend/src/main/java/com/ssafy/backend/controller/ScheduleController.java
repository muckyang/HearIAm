package com.ssafy.backend.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.backend.model.ConRoom;
import com.ssafy.backend.model.Reservation;
import com.ssafy.backend.model.ReserveRes;
import com.ssafy.backend.model.Schedule;
import com.ssafy.backend.model.User;
import com.ssafy.backend.repository.AlarmRepository;
import com.ssafy.backend.repository.ConRoomRepository;
import com.ssafy.backend.repository.ReservationRepository;
import com.ssafy.backend.repository.ScheduleRepository;
import com.ssafy.backend.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ConRoomRepository conRoomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AlarmRepository alarmRepository;

    // @PostMapping("/saveTime/{mentor}")
    // public Object saveTime(@RequestBody Map<Object, int[]> timetable,
    // @PathVariable String mentor) {
    // List<Schedule> list = scheduleRepository.findByMentor(mentor);
    // LocalDate today = LocalDate.now();
    // int todayWeek = DayOfWeek.from(LocalDate.now()).getValue(); // 일 : 0
    // String[] thisWeek = new String[7];
    // for (int i = 0; i <= 6; i++) {
    // if (i < todayWeek) { // 오늘보다 이전이면
    // thisWeek[i] = today.minusDays(todayWeek - i).toString();
    // } else if (i > todayWeek) {
    // thisWeek[i] = today.plusDays(i - todayWeek).toString();
    // } else {
    // thisWeek[i] = today.toString();
    // }
    // }
    // List<Integer>[] timearr = new List[7];

    // for (int i = 0; i < 7; i++) {
    // timearr[i] = new LinkedList<>();
    // }
    // for (Schedule s : list) {
    // if (s.getIsReser() == 0)
    // scheduleRepository.delete(s);
    // else {
    // // 예약되어있는거는 이따가 저장하면안돼
    // }
    // }
    // // 먼저 삭제하고

    // // 등록
    // String newDate = "";
    // String newTime = "";
    // String[] timeArr = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
    // "15:00", "16:00", "17:00", "18:00",
    // "19:00", "20:00", "21:00", "22:00", "23:00" };
    // for (int i = 0; i <= 6; i++) {
    // if (i < todayWeek) { // 오늘보다 이전이면
    // newDate = today.minusDays(todayWeek - i).toString();
    // } else if (i > todayWeek) {
    // newDate = today.plusDays(i - todayWeek).toString();
    // } else {
    // newDate = today.toString();
    // }
    // for (int t : timetable.get(String.valueOf(i))) {
    // newTime = timeArr[t];
    // Schedule schedule = new Schedule();
    // schedule.setMentor(mentor);
    // System.out.println(newTime + " " + LocalTime.parse(newTime));
    // schedule.setSdate(LocalDate.parse(newDate));
    // schedule.setStime(newTime);
    // schedule.setTimeidx(t);
    // scheduleRepository.save(schedule);
    // }
    // }
    // return timetable;
    // }

    @DeleteMapping("/delete")
    public Object DeleteSchedule(@RequestBody Schedule request) {

        Schedule Sche = scheduleRepository.findScheduleByMentorAndSdateAndStime(request.getMentor(), request.getSdate(),
                request.getStime());

        scheduleRepository.delete(Sche);
        return 0;
    }

    @PostMapping("/reservation/{mentee}/{date}/{time}/{concern}")
    public Object reservation(@PathVariable String mentee, @PathVariable String date, @PathVariable String time,
            @PathVariable String concern) {

        // 아직 매칭 안된 스케줄 중에 시간일치하는것 뽑아옴

        List<Schedule> list = scheduleRepository.findScheduleBySdateAndStimeAndIsReser(date,
                time.split(" ")[0], 0);

        // 랜덤돌려서 매칭
        int number = (int) Math.random() * list.size();
        Schedule sche = list.get(number);
        sche.setIsReser(1);
        scheduleRepository.save(sche);
        Reservation reser = new Reservation();
        reser.setScheNum(sche.getNum());
        reser.setMentee(mentee);
        reser.setConcern(concern);
        reservationRepository.save(reser);
        ReserveRes res = new ReserveRes();
        res.setMentor(sche.getMentor());
        res.setSdate(LocalDate.parse(sche.getSdate()));
        res.setStime(sche.getStime());

        return res;
    }

    // @GetMapping("/getTime/{mentor}")
    // public Object getTime(@PathVariable String mentor) {
    // List<Schedule> list = scheduleRepository.findByMentor(mentor);
    // LocalDate today = LocalDate.now();
    // int todayWeek = DayOfWeek.from(LocalDate.now()).getValue(); // 일 : 0
    // String[] thisWeek = new String[7];
    // for (int i = 0; i <= 6; i++) {
    // if (i < todayWeek) { // 오늘보다 이전이면
    // thisWeek[i] = today.minusDays(todayWeek - i).toString();
    // } else if (i > todayWeek) {
    // thisWeek[i] = today.plusDays(i - todayWeek).toString();
    // } else {
    // thisWeek[i] = today.toString();
    // }
    // }
    // List<Integer>[] timearr = new List[7];
    // for (int i = 0; i < 7; i++) {
    // timearr[i] = new LinkedList<>();
    // }
    // for (Schedule s : list) {
    // if (s.getSdate().toString().equals(thisWeek[0])) {
    // timearr[0].add(s.getTimeidx());
    // } else if (s.getSdate().toString().equals(thisWeek[1])) {
    // timearr[1].add(s.getTimeidx());
    // } else if (s.getSdate().toString().equals(thisWeek[2])) {
    // timearr[2].add(s.getTimeidx());
    // } else if (s.getSdate().toString().equals(thisWeek[3])) {
    // timearr[3].add(s.getTimeidx());
    // } else if (s.getSdate().toString().equals(thisWeek[4])) {
    // timearr[4].add(s.getTimeidx());
    // } else if (s.getSdate().toString().equals(thisWeek[5])) {
    // timearr[5].add(s.getTimeidx());
    // } else if (s.getSdate().toString().equals(thisWeek[6])) {
    // timearr[6].add(s.getTimeidx());
    // }
    // }
    // Map<String, List<Integer>> map = new HashMap<>();
    // for (int i = 0; i < 7; i++) {
    // map.put(String.valueOf(i), timearr[i]);
    // }
    // return map;
    // }

    @GetMapping("/getTime/{mentor}")
    public Object getTime(@PathVariable String mentor) {
        List<Schedule> list = scheduleRepository.findByMentor(mentor);
        List<Integer>[] timearr = new List[7];
        for (int i = 0; i < 7; i++) {
            timearr[i] = new LinkedList<>();
        }
        for (Schedule s : list) {
            timearr[s.getDateidx()].add(s.getTimeidx());
        }
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            map.put(String.valueOf(i), timearr[i]);
        }
        return map;
    }

    @GetMapping("/checkTime/{mentor}/{dayidx}/{timeidx}")
    public Object checkTime(@PathVariable String mentor, @PathVariable int dayidx, @PathVariable int timeidx) {
        System.out.println(dayidx + " " + timeidx);
        LocalDate today = LocalDate.now();
        int todayWeek = DayOfWeek.from(LocalDate.now()).getValue();
        LocalDate sdate; // 확인할 날짜
        if (dayidx < todayWeek) { // 오늘보다 이전이면
            sdate = today.minusDays(todayWeek - dayidx);
        } else if (dayidx > todayWeek) {
            sdate = today.plusDays(dayidx - todayWeek);
        } else {
            sdate = today;
        }
        Schedule sche = scheduleRepository.findScheduleByMentorAndSdateAndTimeidx(mentor, sdate.toString(), timeidx);
        if (sche != null) { // 스케줄이 이미 존재하면
            if (sche.getIsReser() == 0) { // 예약이 안되어있으면
                scheduleRepository.delete(sche);
                return -1;
            } else {
                return 0;
            }
        } else { // 스케줄이 없으면
            String stime = "";
            String[] timeArr = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                    "18:00", "19:00", "20:00", "21:00", "22:00", "23:00" };
            stime = timeArr[timeidx];
            Schedule schedule = new Schedule();
            schedule.setMentor(mentor);
            schedule.setSdate(sdate.toString());
            schedule.setStime(stime);
            schedule.setTimeidx(timeidx);
            schedule.setDateidx(dayidx);
            scheduleRepository.save(schedule);
            return 1;
        }

    }

    @GetMapping("/allowSchedule/{mentor}")
    public List<Schedule> counseling(@PathVariable(value = "mentor") String mentor) {
        List<Schedule> list = scheduleRepository.findByMentorAndIsReser(mentor, 0);
        return list;
    }

    @GetMapping("/allowScheduleTime/{mentor}/{sdate}")
    public List<String> counselingTime(@PathVariable(value = "mentor") String mentor, @PathVariable String sdate) {
        List<Schedule> list = scheduleRepository.findByMentorAndSdateAndIsReserOrderByTimeidxAsc(mentor,
                sdate, 0);
        String[] timeArr = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00" };
        List<String> timeList = new LinkedList<>();
        for (Schedule s : list) {
            int idx = s.getTimeidx();
            if (idx == 14) {
                timeList.add(timeArr[idx] + " ~ 24:00");
            } else {
                timeList.add(timeArr[idx] + " ~ " + timeArr[idx + 1]);
            }
        }
        return timeList;
    }

    @GetMapping("/findScheduleNum/{date}/{time}/{mentor}")
    public Long findScheduleNum(@PathVariable(value = "date") String date, @PathVariable(value = "time") String time,
            @PathVariable String mentor) {
        Schedule schedule = scheduleRepository.findScheduleByMentorAndSdateAndStime(mentor, date,
                time);
        Long num = schedule.getNum();
        return num;
    }

    @PostMapping("/reApply/{num}")
    public ResponseEntity<String> reApply(@PathVariable(value = "num") Long num, @RequestBody Reservation reservation) {
        ConRoom conRoom = conRoomRepository.findByNum(num);
        conRoom.setIsreapply(1);
        conRoomRepository.save(conRoom);
        Schedule schedule = scheduleRepository.findByNum(reservation.getScheNum());
        schedule.setIsReser(1);
        scheduleRepository.save(schedule);
        reservationRepository.save(reservation);
        // Alarm alarm = new Alarm();
        // alarm.setMentor(conRoom.getMentor());
        // alarm.setNum(conRoom.getNum());
        return ResponseEntity.ok("success");
    }

    @GetMapping("/isReservation/{mentee}")
    public Boolean isReservation(@PathVariable(value = "mentee") String mentee) {
        Boolean bool = reservationRepository.existsByMentee(mentee);
        return bool;
    }


    //멘토 
    
    // @GetMapping("/myReservation/{mentee}")
    // public List<Schedule> myReservation(@PathVariable(value = "mentee") String mentee) {
    //     List<Reservation> numList = reservationRepository.findByMentee(mentee);
    //     List<Schedule> list = new ArrayList<Schedule>();
        
        
    //     LocalDate today = LocalDate.now();
    //     for (Reservation reservation : numList) {
    //     	Schedule schedule = scheduleRepository.findByNum(reservation.getScheNum());
    //     	if(!schedule.getSdate().isBefore(today)) {
    //     		list.add(schedule);
	// 		}
            
    //     }
    //     list.sort(new Comparator<Schedule>() {
    //         @Override
    //         public int compare(Schedule o1, Schedule o2) {
    //             if (o1.getSdate().getDayOfYear() < o2.getSdate().getDayOfYear()) {
    //                 return -1;
    //             } else if (o1.getSdate().getDayOfYear() == o2.getSdate().getDayOfYear()) {
    //                 return 0;
    //             } else {
    //                 return 1;
    //             }
    //         }
    //     });
        
    //     return list;
    // }
    @GetMapping("/myReservation/{mentee}")
    public List<ConRoom> myReservation(@PathVariable(value = "mentee") String mentee) {
        User user = userRepository.findById(mentee).get();
        System.out.println(mentee);
        List<ConRoom> list = conRoomRepository.findByAfterMenteeReservation(user.getNum(),LocalDateTime.now().plusHours(-1));
        System.out.println(list.toString());
        return list;
    }

    @DeleteMapping("/cancelReservation/{scheNum}")
    public ResponseEntity<String> cancelReservation(@PathVariable(value = "scheNum") Long scheNum) {
        reservationRepository.deleteByScheNum(scheNum);
        Schedule schedule = scheduleRepository.findByNum(scheNum);
        schedule.setIsReser(0);
        scheduleRepository.save(schedule);
        User user = userRepository.findById(schedule.getMentor()).get();
        LocalDateTime date = LocalDateTime.parse(schedule.getSdate()+"T"+schedule.getStime());
        conRoomRepository.deleteByMentorAndDate(user.getNum(), date);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/getTimeByDate/{sdate}")
    public Object getTimeByDate(@PathVariable String sdate) {
        String[] timeArr = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00" };
        List<Schedule> list = scheduleRepository.findBySdateAndIsReserOrderByTimeidxAsc(sdate, 0);
        List<String> timeList = new LinkedList<>();
        for (Schedule s : list) {
            int idx = s.getTimeidx();
            if (idx == 14) {
                timeList.add(timeArr[idx] + " ~ 24:00");
            } else {
                timeList.add(timeArr[idx] + " ~ " + timeArr[idx + 1]);
            }
        }
        return timeList;
    }
    
    @GetMapping("/siganHwakIn/{sdate}")
    public LocalDate siganHwakIn(@PathVariable String sdate) {
    	return LocalDate.parse(sdate);
    }

    @DeleteMapping("/reset/{mentor}")
    public Object reset(@PathVariable String mentor) {
        User umentor = userRepository.findById(mentor).get();
        umentor.setIsSet(1);
        userRepository.save(umentor);

        List<Schedule> list = scheduleRepository.findByMentor(mentor);
        for (Schedule s : list) {
            scheduleRepository.delete(s);
        }
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            List<Integer> temp = new LinkedList<>();
            map.put(String.valueOf(i), temp);
        }
        return map;
    }

    @PutMapping("/maintain/{mentor}")
    public Object maintain(@PathVariable String mentor) {
        User umentor = userRepository.findById(mentor).get();
        umentor.setIsSet(1);
        userRepository.save(umentor);
        List<Schedule> list = scheduleRepository.findByMentor(mentor);
        LocalDate today = LocalDate.now();
        System.out.println(today);
        int todayWeek = DayOfWeek.from(LocalDate.now()).getValue();
        for (Schedule s : list) {
            LocalDate sdate;
            int dayidx = DayOfWeek.from(LocalDate.parse(s.getSdate())).getValue();
            if (dayidx < todayWeek) { // 오늘보다 이전이면
                sdate = today.minusDays(todayWeek - dayidx);
            } else if (dayidx > todayWeek) {
                sdate = today.plusDays(dayidx - todayWeek);
            } else {
                sdate = today;
            }
            s.setSdate(sdate.toString());
            scheduleRepository.save(s);
        }
        return list;
    }

    @GetMapping("/checkUpdate/{id}")
    public Object checkUpdate(@PathVariable String id) {
        User user = userRepository.findById(id).get();
        if (user.getIsSet() == 1) {
            return 1;
        } else {
            return 0;// 설정 띄워야 됨
        }
    }

    @Scheduled(cron = "0 0 0 * * 1")
    public void cronJobSch() { // 월요일마다 isset을 0으로 초기화
        List<User> list = userRepository.findAll();
        for (User u : list) {
            u.setIsSet(0);
            userRepository.save(u);
        }
    }

}
