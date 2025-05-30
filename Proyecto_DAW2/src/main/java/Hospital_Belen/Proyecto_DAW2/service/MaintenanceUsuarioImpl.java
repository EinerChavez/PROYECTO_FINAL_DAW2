package Hospital_Belen.Proyecto_DAW2.service;

import Hospital_Belen.Proyecto_DAW2.dto.UsuarioDTO;
import Hospital_Belen.Proyecto_DAW2.entity.Usuario;
import Hospital_Belen.Proyecto_DAW2.repository.UsuarioRepository;
import Hospital_Belen.Proyecto_DAW2.service.impl.MaintenanceUsuario;
import Hospital_Belen.Proyecto_DAW2.service.impl.UsuarioNoValidoException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class MaintenanceUsuarioImpl implements MaintenanceUsuario {

    private final UsuarioRepository usuarioRepository;

    public MaintenanceUsuarioImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDTO usuarioValidado(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoValidoException("Usuario no encontrado"));

        if (!usuario.getContrasenia().equals(password)) {
            throw new UsuarioNoValidoException("Contraseña incorrecta");
        }
        return new UsuarioDTO(usuario.getIdUsuario(), usuario.getNombre(), usuario.getEmail());
    }
    @Override
    public Integer idUsuario(Principal principal) throws Exception {
        return null;
    }
}
