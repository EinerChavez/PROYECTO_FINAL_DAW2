package Hospital_Belen.Proyecto_DAW2.controller;

import Hospital_Belen.Proyecto_DAW2.dto.MedicoDTO;
import Hospital_Belen.Proyecto_DAW2.dto.PacienteDTO;
import Hospital_Belen.Proyecto_DAW2.dto.UsuarioDTO;
import Hospital_Belen.Proyecto_DAW2.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/paciente")
    public ResponseEntity<?> registrarPaciente(@RequestBody PacienteDTO dto) {
        try {
            usuarioService.registrarPaciente(
                    dto.nombre, dto.apellido, dto.sexo,
                    dto.telefono, dto.email, dto.contrasenia,
                    dto.fechaNacimiento, dto.talla, dto.grupoSanguineo,
                    dto.direccion
            );
            return ResponseEntity.ok(Map.of("message", "Paciente registrado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al registrar paciente: " + e.getMessage()));
        }
    }

    @PostMapping("/medico")
    public ResponseEntity<?> registrarMedico(@RequestBody MedicoDTO dto) {
        try {
            usuarioService.registrarMedico(
                    dto.nombre, dto.apellido, dto.sexo,
                    dto.telefono, dto.email, dto.contrasenia,
                    dto.especialidad, dto.numeroColegiado, dto.codigoHospital
            );
            return ResponseEntity.ok(Map.of("message", "Médico registrado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al registrar médico: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioDesdeVista(@PathVariable Integer id) {
        return usuarioService.obtenerUsuarioDesdeVistaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(
                        Map.of(
                                "error", "Usuario no encontrado",
                                "detalle", "No existe un usuario con el ID " + id,
                                "codigo", 404
                        )
                ));
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validarUsuario(@RequestParam String email, @RequestParam String password) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.usuarioValidado(email, password);
            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Credenciales inválidas", "detalle", e.getMessage()));
        }
    }
}
