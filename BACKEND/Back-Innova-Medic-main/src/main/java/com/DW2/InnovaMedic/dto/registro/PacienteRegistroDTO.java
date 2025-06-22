package com.DW2.InnovaMedic.dto.registro;

import com.DW2.InnovaMedic.entity.Usuario;

import java.time.LocalDate;

public record PacienteRegistroDTO(
        String nombre,
        String apellido,
        Usuario.Sexo sexo,
        String telefono,
        String email,
        String contrasenia,
        LocalDate fechaNacimiento,
        String talla,
        String grupoSanguineo,
        String direccion
) {
}
