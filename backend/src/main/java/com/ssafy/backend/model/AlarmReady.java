package com.ssafy.backend.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@Table(name = "alarm_ready")
public class AlarmReady {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long num ;
    private Long mentor;

}