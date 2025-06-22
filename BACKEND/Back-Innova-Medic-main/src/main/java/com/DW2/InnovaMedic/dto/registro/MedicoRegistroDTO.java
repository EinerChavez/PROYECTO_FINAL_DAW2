package com.DW2.InnovaMedic.dto.registro;

import com.DW2.InnovaMedic.entity.Usuario;

public record MedicoRegistroDTO(
        String nombre,
        String apellido,
        Usuario.Sexo sexo,
        String telefono,
        String email,
        String contrasenia,
        String especialidad,
        String numeroColegiado,
        String codigoHospital
) {
}
