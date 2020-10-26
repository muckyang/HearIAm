package com.ssafy.backend.model;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;

@Entity
@Data
@Table(name = "con_room")
public class ConRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    private Long mentor;
    private Long mentee;
    private String room;
    private Date date;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String path;
    private String status;
}
