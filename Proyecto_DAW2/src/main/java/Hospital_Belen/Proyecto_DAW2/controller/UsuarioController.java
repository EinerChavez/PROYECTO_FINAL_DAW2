package Hospital_Belen.Proyecto_DAW2.controller;

import Hospital_Belen.Proyecto_DAW2.dto.MedicoDTO;
import Hospital_Belen.Proyecto_DAW2.dto.PacienteDTO;
import Hospital_Belen.Proyecto_DAW2.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/paciente")
    public ResponseEntity<Void> registrarPaciente(@RequestBody PacienteDTO dto) {
        usuarioService.registrarPaciente(
                dto.nombre, dto.apellido, dto.sexo,
                dto.telefono, dto.email, dto.contrasenia,
                dto.fechaNacimiento, dto.talla, dto.grupoSanguineo,
                dto.direccion
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/medico")
    public ResponseEntity<Void> registrarMedico(@RequestBody MedicoDTO dto) {
        usuarioService.registrarMedico(
                dto.nombre, dto.apellido, dto.sexo,
                dto.telefono, dto.email, dto.contrasenia,
                dto.especialidad, dto.numeroColegiado, dto.codigoHospital
        );
        return ResponseEntity.ok().build();
    }
}
