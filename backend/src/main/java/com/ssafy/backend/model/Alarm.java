package com.ssafy.backend.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@Table(name = "alarm")
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long num ;
    private Long mentee;
    private String room;
    private Long mentor;

    public Alarm(Long mentee, String room) {
        this.mentee = mentee;
        this.room = room;
    }
    
    
}
