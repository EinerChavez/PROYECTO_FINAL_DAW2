package com.DW2.InnovaMedic.dto.log;

public record UsuarioDTO(
        Integer idUsuario,
        String nombre,
        String apellido,
        String rol
) {
}
