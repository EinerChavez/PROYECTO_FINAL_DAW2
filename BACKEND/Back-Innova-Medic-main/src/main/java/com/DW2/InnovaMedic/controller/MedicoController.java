package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoResumenDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @GetMapping("/{idMedico}")
    public ResponseEntity<?> obtenerHorariosCompletos(
            @PathVariable Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (idMedico == null || idMedico <= 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errorMsg", "ID de médico inválido",
                    "errorCode", "INVALID_ID"
            ));
        }

        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errorMsg", "La fecha debe ser igual o posterior al día actual",
                    "errorCode", "INVALID_DATE"
            ));
        }

        try {
            Map<String, List<String>> horarios = maintenanceMedico.obtenerHorariosDisponiblesYOcupados(idMedico, fecha);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "medicoId", idMedico,
                            "fecha", fecha.toString(),
                            "horariosDisponibles", horarios.get("disponibles"),
                            "horariosOcupados", horarios.get("ocupados")
                    ),
                    "message", "Horarios obtenidos exitosamente"
            ));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "errorMsg", e.getMessage(),
                    "errorCode", "MEDICO_NOT_FOUND"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errorMsg", e.getMessage(),
                    "errorCode", "INVALID_PARAMETER"
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "errorMsg", "Error interno al obtener horarios",
                    "errorCode", "INTERNAL_ERROR",
                    "debugMessage", e.getMessage()
            ));
        }
    }
}