package com.ssafy.backend.controller;

import java.util.List;

import com.ssafy.backend.model.Center;
import com.ssafy.backend.repository.CenterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/center")
public class MapController {
    @Autowired
    CenterRepository centerRepository;

    @GetMapping("/getCenter/{word}")
    public Object getCenter(@PathVariable(value = "word") String word) {
        List<Center> list = null;
        if (word.equals("default") || word.equals("전체")) {
            list = centerRepository.findAll();
        } else {
            word = "%"+word+"%";
            list = centerRepository.findByAddressLike(word);
            System.out.println(list.size());
        }
        return list;
    }

}
