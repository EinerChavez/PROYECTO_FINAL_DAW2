package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.Medico;

public record MedicoResumenDTO(
        Integer idUsuario,
        String nombre,
        String apellido,
        String especialidad
) {
    public static MedicoResumenDTO fromEntity(Medico medico) {
        return new MedicoResumenDTO(
                medico.getIdUsuario(),
                medico.getNombre(),
                medico.getApellido(),
                medico.getEspecialidad()
        );
    }
}
