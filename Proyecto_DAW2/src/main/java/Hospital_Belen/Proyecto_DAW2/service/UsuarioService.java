package Hospital_Belen.Proyecto_DAW2.service;

import Hospital_Belen.Proyecto_DAW2.dto.MedicoDTO;
import Hospital_Belen.Proyecto_DAW2.dto.PacienteDTO;
import Hospital_Belen.Proyecto_DAW2.dto.UsuarioDTO;
import Hospital_Belen.Proyecto_DAW2.entity.Usuario;
import Hospital_Belen.Proyecto_DAW2.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final EntityManager entityManager;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void registrarPaciente(String nombre, String apellido, String sexo, String telefono,
                                  String email, String contrasenia, LocalDate fechaNacimiento,
                                  String talla, String grupoSanguineo, String direccion) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("registrar_paciente")
                    .registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_apellido", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_sexo", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_telefono", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_contrasenia", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_fecha_nacimiento", LocalDate.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_talla", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_grupo_sanguineo", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_direccion", String.class, ParameterMode.IN)
                    .setParameter("p_nombre", nombre)
                    .setParameter("p_apellido", apellido)
                    .setParameter("p_sexo", sexo)
                    .setParameter("p_telefono", telefono)
                    .setParameter("p_email", email)
                    .setParameter("p_contrasenia", contrasenia)
                    .setParameter("p_fecha_nacimiento", fechaNacimiento)
                    .setParameter("p_talla", talla)
                    .setParameter("p_grupo_sanguineo", grupoSanguineo)
                    .setParameter("p_direccion", direccion);

            query.execute();

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar paciente: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void registrarMedico(String nombre, String apellido, String sexo, String telefono,
                                String email, String contrasenia, String especialidad,
                                String numeroColegiado, String codigoHospital) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("registrar_medico")
                    .registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_apellido", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_sexo", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_telefono", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_contrasenia", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_especialidad", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_numero_colegiado", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("p_codigo_hospital", String.class, ParameterMode.IN)
                    .setParameter("p_nombre", nombre)
                    .setParameter("p_apellido", apellido)
                    .setParameter("p_sexo", sexo)
                    .setParameter("p_telefono", telefono)
                    .setParameter("p_email", email)
                    .setParameter("p_contrasenia", contrasenia)
                    .setParameter("p_especialidad", especialidad)
                    .setParameter("p_numero_colegiado", numeroColegiado)
                    .setParameter("p_codigo_hospital", codigoHospital);

            query.execute();

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar médico: " + e.getMessage(), e);
        }
    }

    public Optional<Object> obtenerUsuarioDesdeVistaPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if ("Paciente".equalsIgnoreCase(usuario.getRol())) {
                        return new PacienteDTO(
                                usuario.getNombre(),
                                usuario.getApellido(),
                                usuario.getSexo(),
                                usuario.getTelefono(),
                                usuario.getEmail(),
                                usuario.getContrasenia(),
                                null, // Suponiendo que la vista no devuelve fecha
                                null,
                                null,
                                null
                        );
                    } else if ("Medico".equalsIgnoreCase(usuario.getRol())) {
                        return new MedicoDTO(
                                usuario.getNombre(),
                                usuario.getApellido(),
                                usuario.getSexo(),
                                usuario.getTelefono(),
                                usuario.getEmail(),
                                usuario.getContrasenia(),
                                null,
                                null,
                                null
                        );
                    } else {
                        return null;
                    }
                });
    }

    public UsuarioDTO usuarioValidado(String email, String password) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);

        if (optionalUsuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
        }

        Usuario usuario = optionalUsuario.get();

        if (!usuario.getContrasenia().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña incorrecta");
        }

        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}
