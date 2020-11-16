package com.ssafy.backend.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRes {
    String mentor;
    LocalDate sdate;
    String stime;
}
