package com.DW2.InnovaMedic.dto.perfil;

import com.DW2.InnovaMedic.entity.Usuario;

public record MedicoPerfilDTO(
        Usuario.Sexo sexo,
        String telefono,
        String email,
        String especialidad,
        String numeroColegiado
) {
}
