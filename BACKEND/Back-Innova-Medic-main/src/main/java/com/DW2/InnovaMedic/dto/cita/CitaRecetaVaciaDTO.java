package com.DW2.InnovaMedic.dto.cita;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaRecetaVaciaDTO(
        Integer idCita,
        String instruccionesAdicionales,
        String firmaMedico
) {
}
