package com.ssafy.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

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

import com.ssafy.backend.exception.ResourceNotFoundException;
import com.ssafy.backend.model.ConReport;
import com.ssafy.backend.model.ConRoom;
import com.ssafy.backend.model.Emotion;
import com.ssafy.backend.model.User;
import com.ssafy.backend.repository.ConReportRepository;
import com.ssafy.backend.repository.ConRoomRepository;
import com.ssafy.backend.repository.EmotionRepository;
import com.ssafy.backend.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/counseling")
public class CounselingController {

	private static final String SUCCESS = "success";
	
	@Autowired
	ConRoomRepository conRoomRepository;

	@Autowired
	ConReportRepository conReportRepository;
	
	@Autowired
	EmotionRepository emotionRepository;
	
	@Autowired
	UserRepository userRepository;

	@PostMapping("/liveRequest")
	public ResponseEntity<String> liveRequest(@RequestBody ConRoom conRoom) {
		conRoomRepository.save(conRoom);

		return ResponseEntity.ok(SUCCESS);
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
	public ResponseEntity<String> finishLive(@PathVariable(value = "num") Long num) {
		ConRoom conRoom = conRoomRepository.findByNum(num);
		conRoom.setStatus("finish");
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

	@PostMapping("/saveMemo")
	public ResponseEntity<String> saveMemo(@RequestBody ConReport conReport) {
		conReportRepository.save(conReport);

		return ResponseEntity.ok(SUCCESS);
	}
}
