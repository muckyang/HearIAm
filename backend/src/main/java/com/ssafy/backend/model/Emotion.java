package com.ssafy.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@Table(name = "emo_data")
public class Emotion {
	@Id
	@JoinColumn(name="con_room_num")
	private Long num;
	private String angry;
	private String disgusted;
	private String fearful;
	private String happy;
	private String neutral;
	private String sad;
	private String surprised;
}
