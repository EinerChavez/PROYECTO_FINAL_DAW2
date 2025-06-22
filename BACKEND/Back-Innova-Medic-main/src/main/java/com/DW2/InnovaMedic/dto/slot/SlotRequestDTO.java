package com.DW2.InnovaMedic.dto.slot;

import java.time.LocalDate;

public record SlotRequestDTO(
        Integer idMedico,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}
