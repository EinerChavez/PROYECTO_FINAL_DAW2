package com.DW2.InnovaMedic.dto.perfil;

import com.DW2.InnovaMedic.entity.Usuario;

import java.time.LocalDate;

public record PacientePerfilDTO(
        Usuario.Sexo sexo,
        String telefono,
        String email,
        LocalDate fechaNacimiento,
        String talla,
        String grupoSanguineo,
        String direccion
) {
}
