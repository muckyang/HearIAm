package com.ssafy.backend.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
// @MockBean
@Table(name = "con_room")
public class ConRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="num")
    private Long num;
    private Long mentor;
    private Long mentee;
    private String title;
    private String room;
    private LocalDateTime date;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String recordDir;
    private String wordcloudImg;
    private String status;
    private int isreapply;
    private String answer;
    private String memo;


}