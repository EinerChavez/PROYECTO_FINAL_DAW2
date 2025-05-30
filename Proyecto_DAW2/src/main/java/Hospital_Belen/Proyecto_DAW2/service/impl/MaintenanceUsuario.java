package Hospital_Belen.Proyecto_DAW2.service.impl;

import Hospital_Belen.Proyecto_DAW2.dto.UsuarioDTO;

import java.security.Principal;

public interface MaintenanceUsuario {
    UsuarioDTO usuarioValidado(String email, String password);

    Integer idUsuario(Principal principal) throws Exception;
}
