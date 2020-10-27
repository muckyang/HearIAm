package com.ssafy.backend.model.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.Valid;

import lombok.Data;

@Data
@Valid
public class ReserRequest {
    LocalDate sdate;
    String stime;
    String mentee;
}
