package com.DW2.InnovaMedic.dto.cita;

public record MedicamentoRecetaRequestDTO(
        String medicamento,
        String dosis,
        String frecuencia,
        String duracion,
        String Observaciones
) { }
