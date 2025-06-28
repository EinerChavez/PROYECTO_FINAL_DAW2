package com.DW2.InnovaMedic.dto.cita;

import java.time.LocalDate;
import java.time.LocalTime;

public record RegistrarCitaDTO(
        Integer idMedico,
        Integer idPaciente,
        LocalDate fecha,
        LocalTime hora) {
}
