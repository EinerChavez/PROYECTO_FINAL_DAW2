package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.registro.PacienteRegistroDTO;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    @Autowired
    MaintenancePaciente maintenancePaciente;

    @GetMapping("/cita/{id}")
    public ResponseEntity<?> listaCitasPaciente(@PathVariable Integer id) {
        try {
            List<CitaDTO> citasPaciente = maintenancePaciente.obtenerCitasPaciente(id);

            if (citasPaciente.isEmpty()) {
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", HttpStatus.OK.value(),
                                "message", "El paciente existe, pero no tiene citas registradas.",
                                "idPaciente", id
                        )
                );
            }

            return ResponseEntity.ok(citasPaciente);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", HttpStatus.NOT_FOUND.value(),
                            "error", "Paciente no encontrado",
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
    public ResponseEntity<?> registrarPaciente(@RequestBody PacienteRegistroDTO pacienteRegistroDTO) {
        try {
            maintenancePaciente.registrarPaciente(pacienteRegistroDTO);
            return ResponseEntity.ok(Map.of("message", "Usuario registrado con exito"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errorMsg", "Hubo error al registrar usuario: " + e.getMessage()));
        }
    }
}
