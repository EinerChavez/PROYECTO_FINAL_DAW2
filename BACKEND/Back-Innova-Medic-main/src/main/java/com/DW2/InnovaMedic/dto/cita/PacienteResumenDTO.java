package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.Paciente;

public record PacienteResumenDTO(
        Integer idUsuario,
        String nombre,
        String apellido,
        String grupoSanguineo
) {
    public static PacienteResumenDTO fromEntity(Paciente paciente) {
        return new PacienteResumenDTO(
                paciente.getIdUsuario(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getGrupoSanguineo()
        );
    }
}
