package com.DW2.InnovaMedic.dto.projections;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CitaHorarioProjection {
    LocalDate getFecha();
    LocalTime getHora();
}
