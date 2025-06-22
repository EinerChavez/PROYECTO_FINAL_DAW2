package com.DW2.InnovaMedic.dto.cita;

public record ActualizarCitaCompletaDTO(
        Integer id,
        ActionCitaMedicoDTO actionCitaMedicoDTO,
        String nombreMedico
) {
}
