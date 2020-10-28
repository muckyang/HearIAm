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
@Table(name = "reservation")
public class Reservation {
    @Id
    long num;
    String mentee;
    long scheNum;
    String concern;
    
}
