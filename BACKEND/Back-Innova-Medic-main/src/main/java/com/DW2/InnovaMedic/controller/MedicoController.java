package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoResumenDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
    @Autowired
    MaintenanceMedico maintenanceMedico;
    @Autowired
    private MedicoRepository medicoRepository;
    @GetMapping
    public ResponseEntity<?> listarTodosLosMedicos() {
        try {
            List<Medico> medicos = medicoRepository.findAll();

            if (medicos.isEmpty()) {
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", HttpStatus.OK.value(),
                                "message", "No hay médicos registrados en el sistema",
                                "count", 0
                        )
                );
            }

            List<MedicoResumenDTO> medicosResumen = medicos.stream()
                    .map(medico -> new MedicoResumenDTO(
                            medico.getIdUsuario(),
                            medico.getNombre(),
                            medico.getApellido(),
                            medico.getEspecialidad()
                    ))
                    .toList();

            return ResponseEntity.ok().body(
                    Map.of(
                            "status", HttpStatus.OK.value(),
                            "count", medicosResumen.size(),
                            "medicos", medicosResumen
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Error al obtener listado de médicos",
                            "message", e.getMessage()
                    )
            );
        }
    }
    @GetMapping("/cita/{id}")
    public ResponseEntity<?> listaCitasMedico(@PathVariable Integer id) {
        try {
            List<CitaDTO> citasMedico = maintenanceMedico.obtenerCitasMedico(id);

            if (citasMedico.isEmpty()) {
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", HttpStatus.OK.value(),
                                "message", "El medico existe, pero no tiene citas registradas.",
                                "idMedico", id
                        )
                );
            }

            return ResponseEntity.ok(citasMedico);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", HttpStatus.NOT_FOUND.value(),
                            "error", "Medico no encontrado",
                            "message", ie.getMessage(),
                            "idSolicitado", id
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Error interno al buscar citas",
                            "message", "Error de búsqueda con código " + id + ": " + e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMedico(@RequestBody MedicoRegistroDTO medicoRegistroDTO) {
        try {
            maintenanceMedico.registrarMedicos(medicoRegistroDTO);
            return ResponseEntity.ok(Map.of("message", "Usuario registrado con exito"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errorMsg", "Hubo error al registrar usuario: " + e.getMessage()));
        }
    }

}
