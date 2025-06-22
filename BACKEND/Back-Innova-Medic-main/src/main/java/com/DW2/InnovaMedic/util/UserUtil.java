package com.DW2.InnovaMedic.util;

import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.entity.Usuario;

public class UserUtil {
    public static String role (Usuario usuario) {
        String rol = "Desconocido";

        if (usuario instanceof Paciente) {
            rol = "Paciente";
        } else if (usuario instanceof Medico) {
            rol = "Medico";
        }

        return rol;
    }
}
