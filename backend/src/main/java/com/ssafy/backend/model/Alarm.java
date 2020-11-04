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
    private Long mentor;
    
    public Alarm(Long mentor, Long num) {
        this.mentor = mentor;
        this.num = num;
    }
    
    @OneToOne
    @JoinColumn(name = "num")
    private ConRoom conRoom;
}
