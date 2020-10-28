package com.ssafy.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@Table(name = "con_room")
public class ConRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    private Long mentor;
    private Long mentee;
    private String title;
    private String room;
    @Column(insertable = false, updatable = false)
    private LocalDate date;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String recordDir;
    private String wordcloudImg;
    private String status;
    private int isreapply;
}