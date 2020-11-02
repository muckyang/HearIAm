package com.ssafy.backend.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.hibernate.type.LocalDateTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import io.swagger.annotations.ApiOperation;

import com.google.common.base.Optional;
import com.ssafy.backend.exception.ResourceNotFoundException;
import com.ssafy.backend.model.Alarm;
import com.ssafy.backend.model.AlarmReady;
import com.ssafy.backend.model.ConRoom;
import com.ssafy.backend.model.Emotion;
import com.ssafy.backend.model.Schedule;
import com.ssafy.backend.model.User;
import com.ssafy.backend.repository.AlarmReadyRepository;
import com.ssafy.backend.repository.AlarmRepository;
import com.ssafy.backend.repository.ConRoomRepository;
import com.ssafy.backend.repository.EmotionRepository;
import com.ssafy.backend.repository.ScheduleRepository;
import com.ssafy.backend.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/counseling")
public class CounselingController {

	private static final String SUCCESS = "success";
		
	@Autowired
	AlarmReadyRepository alarmReadyRepository;

	@Autowired
	AlarmRepository alarmRepository;

	@Autowired
	ConRoomRepository conRoomRepository;
	
	@Autowired
	EmotionRepository emotionRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ScheduleRepository scheduleRepository;

	@PostMapping("/liveRequest")
	public ResponseEntity<Long> liveRequest(@RequestBody ConRoom conRoom) {
		conRoomRepository.save(conRoom);
		Alarm alarm = new Alarm(conRoom.getMentee(), conRoom.getRoom());
		alarmRepository.save(alarm);
		ConRoom conRoom2 = conRoomRepository.findByRoom(conRoom.getRoom());
		return ResponseEntity.ok(conRoom2.getNum());

	}
	
	@PostMapping("/saveEmotion")
	public ResponseEntity<String> saveEmotion(@RequestBody Emotion emotion) {
		emotionRepository.save(emotion);

		return ResponseEntity.ok(SUCCESS);
	}
	
	@GetMapping("/loadEmotion/{num}")
	public Emotion loadEmotion(@PathVariable(value = "num") Long num) {
		Emotion emotion = emotionRepository.findByNum(num);
		return emotion;
	}
	
	@GetMapping("/liveList")
	public List<ConRoom> liveList() {
		List<ConRoom> list = conRoomRepository.findByStatus("liveRequest");
		Date now = new Date();
		
		for (ConRoom conRoom : list) {
			Date date2 = java.sql.Timestamp.valueOf(conRoom.getDate());
			if((now.getTime()-date2.getTime())/60000 > 30) {
				conRoomRepository.deleteByNum(conRoom.getNum());
			}
		}
		list = conRoomRepository.findByStatus("liveRequest");
		return list;
	}
	
	@GetMapping("/counseling/{num}")
	public ConRoom counseling(@PathVariable(value = "num") Long num) {
		ConRoom conRoom = conRoomRepository.findByNum(num);
		return conRoom;
	}
	
	@PutMapping("/joinLive/{num}/{mentorNum}")
	public ResponseEntity<String> joinLive(@PathVariable(value = "num") Long num,@PathVariable(value = "mentorNum") Long mentorNum) {
		ConRoom conRoom = conRoomRepository.findByNum(num);
		conRoom.setMentor(mentorNum);
		conRoom.setStatus("progress");
		conRoomRepository.save(conRoom);

		return ResponseEntity.ok(SUCCESS);
	}
	
	@PutMapping("/finishLive/{num}")
	public ResponseEntity<String> finishLive(@PathVariable(value = "num") Long num, @RequestBody ConRoom con) {
		ConRoom conRoom = conRoomRepository.findByNum(num);
		conRoom.setStatus("finish");
		conRoom.setTitle(con.getTitle());
		conRoom.setMemo(con.getMemo());
		conRoomRepository.save(conRoom);

		return ResponseEntity.ok(SUCCESS);
	}
	
	@DeleteMapping("/liveCancel/{num}")
	public ResponseEntity<String> liveCancel(@PathVariable(value = "num") Long num) {
		conRoomRepository.deleteByNum(num);

		return ResponseEntity.ok(SUCCESS);
	}

	@GetMapping("/menteeMyList/{num}")
	public List<ConRoom> myList(@PathVariable(value = "num") Long num) {
		List<ConRoom> list = conRoomRepository.findByMenteeOrderByNumDesc(num);
		return list;
	}
	
	@GetMapping("/myMenteeList/{num}")
	public List<User> myMenteeList(@PathVariable(value = "num") Long num) {
		List<ConRoom> list = conRoomRepository.findByMentor(num);
		List<Long> result = new ArrayList<Long>();
		for (ConRoom conRoom : list) {
			result.add(conRoom.getMentee());
		}
		TreeSet<Long> distinctData = new TreeSet<Long>(result);
		result =  new ArrayList<Long>(distinctData);
		
		List<User> users = new ArrayList<User>();
		for (Long long1 : result) {
			users.add(userRepository.findByNum(long1).orElseThrow(() -> new ResourceNotFoundException("User", "num", num)));
		}
		
		return users;
	}
	
	@GetMapping("/myMenteeInfoList/{mentor}/{mentee}")
	public List<ConRoom> myMenteeInfoList(@PathVariable(value = "mentor") Long mentor, @PathVariable(value = "mentee") Long mentee) {
		List<ConRoom> list = conRoomRepository.findByMentorAndMentee(mentor, mentee);
		return list;
	}

	@GetMapping("/RecordList/{mentor}")
	public List<ConRoom>  myRecordCounList(@PathVariable(value = "mentor" ) Long mentor ){
		List<ConRoom> list = conRoomRepository.findByMentorAndStatus(mentor,"progress");	
		return list;
	}
	
	@GetMapping("/liveMenteeInfo/{num}")
	public User liveMenteeInfo(@PathVariable(value = "num") Long num) {
		ConRoom conRoom = conRoomRepository.findByNum(num);
		User user = userRepository.findByNum(conRoom.getMentee()).orElseThrow(() -> new ResourceNotFoundException("User", "num", num));
		return user;
	}
	
	@GetMapping("/ReserveList/{mentor}")
	public List<ConRoom> ReserveList(@PathVariable(value = "mentor") Long mentor){
		List<ConRoom> list = conRoomRepository.findByMentorAndStatusOrStatusOrderByDateAsc(mentor,"reserve","reapply");	
		for (ConRoom conRoom : list) {
			conRoom.setDate(conRoom.getDate().minusHours(9));
		}
		return list;
	}
	
	@PostMapping("/reserveRequest")
	public ResponseEntity<String> reserveRequest(@RequestBody ConRoom conRoom) {
		conRoom.setDate(conRoom.getDate().plusHours(9));
		conRoomRepository.save(conRoom);

		return ResponseEntity.ok(SUCCESS);
	}

	@PutMapping("/update")
	@ApiOperation("상담일지 수정등록")
	public Object updateReport(@RequestBody ConRoom request) throws IOException, SQLException {
		try {
			conRoomRepository.save(request);
			return new ResponseEntity<>("report save", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/addReady/{mentor}")
	public Object addReady(@PathVariable(value = "mentor") Long mentor){
		try {
			AlarmReady ready = new AlarmReady();
			ready.setMentor(mentor);
			alarmReadyRepository.save(ready);
			return new ResponseEntity<>("readyList save", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@GetMapping("/isMentee")
	public Object isMentee(){
		try {
			long cnt = alarmReadyRepository.count();
			return new ResponseEntity<>(cnt, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/isRoom/{mentor}/{roomNum}")
	public Object isMentee(@PathVariable(value = "mentor") Long mentor, @PathVariable(value = "roomNum") String roomNum){
		try {
			System.out.println(roomNum);
			Optional<Alarm> alarm = alarmRepository.findByRoom(roomNum);
			System.out.println(alarm);
			String result = "";
			if(!alarm.isPresent()){
				result = "fail";
			}else{
				System.out.println("success");
				alarmRepository.delete(alarm.get());
				alarmReadyRepository.deleteByMentor(mentor);
				result = "sucess";
			}
			System.out.println(result);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/reserveConRoom/{num}")
	public ConRoom reserveConRoom(@PathVariable(value = "num") Long num) {
		Schedule schedule = scheduleRepository.findByNum(num);
		User user = userRepository.findById(schedule.getMentor()).orElseThrow(() -> new ResourceNotFoundException("User", "id", schedule.getMentor()));
		LocalDateTime date = LocalDateTime.parse(schedule.getSdate()+"T"+schedule.getStime());
		ConRoom conRoom = conRoomRepository.findByMentorAndDate(user.getNum(), date.plusHours(9));
		
		return conRoom;
	}

	@GetMapping("/alarmList")
	public List<Alarm> alarmList(){
		List<Alarm> list = alarmRepository.findAll();
		return list;
	}

	@GetMapping("/alarmListCnt")
	public Long alarmListCnt(){
		Long cnt = alarmRepository.count();
		return cnt;
	}

	@GetMapping("/getMenteeCnt")
	public Object getMenteeCnt(){
		if(alarmRepository.count() >0){
			List<Alarm> list = alarmRepository.findAll();
			return new ResponseEntity<>(list.get(0), HttpStatus.OK);
		}else{
			return new ResponseEntity<>("empty", HttpStatus.OK);
		}
	}

	
	@DeleteMapping("/deleteReadyMentor/{mentor}")
	public void deleteReadyMentor(@PathVariable (value = "mentor") Long mentor){
		alarmReadyRepository.deleteByMentor(mentor);
	}

	@DeleteMapping("/deleteReadyMentee/{mentee}")
	public void deleteReadyMentee(@PathVariable (value = "mentee") Long mentee){
		alarmRepository.deleteByMentee(mentee);
	}
}
