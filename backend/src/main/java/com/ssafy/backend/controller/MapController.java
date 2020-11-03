package com.ssafy.backend.controller;

import java.util.List;

import com.ssafy.backend.model.Center;
import com.ssafy.backend.repository.CenterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/center")
public class MapController {
    @Autowired
    CenterRepository centerRepository;
	
	@GetMapping("/getCenter")
	public Object getCenter() {
        List<Center> list = centerRepository.findAll();
		return list;
	}
	
}
