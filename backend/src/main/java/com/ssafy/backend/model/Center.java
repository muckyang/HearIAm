package com.ssafy.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@Table(name = "center")
public class Center {
    @Id
    long num;
    String name;
    String phone;
    double lat;
    double lon;
    String address;

}
