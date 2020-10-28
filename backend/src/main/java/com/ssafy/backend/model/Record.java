package com.ssafy.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "record")
public class Record {
    @Id
    long num;
    String title;
    String filename;
    String mentee;
    int isAssigned;

}
