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
    @JoinColumn(name="con_room_num")
	private Long num ;
    private Long mentor;
    
    public Alarm( Long num, Long mentor) {
        this.num = num;
        this.mentor = mentor;
    }
    
    @OneToOne
    @JoinColumn(name = "num")
    private ConRoom conRoom;
}
