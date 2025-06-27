package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.MedicamentoRecetaDTO;
import com.DW2.InnovaMedic.service.MaintenanceMedicamento;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/medicamentos")
@AllArgsConstructor
public class MedicamentosController {

    private final MaintenanceMedicamento service;

    @GetMapping("/listar-por-receta/{idReceta}")
    public ResponseEntity<?> listarPorIdReceta(@PathVariable Integer idReceta) {
        try {
            var medicamentos = service.listarPorIdReceta(idReceta);

            if (medicamentos.isEmpty()) {
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", HttpStatus.OK.value(),
                                "message", "No se encontraron medicamentos para la receta indicada.",
                                "count", 0
                        )
                );
            }

            return ResponseEntity.ok().body(
                    Map.of(
                            "status", HttpStatus.OK.value(),
                            "count", medicamentos.size(),
                            "medicamentos", medicamentos
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Error al listar medicamentos por receta",
                            "message", e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMedicamento(@RequestBody MedicamentoRecetaDTO dto) {
        try {
            Integer id = service.registrarMedicamento(dto);

            return ResponseEntity.ok().body(
                    Map.of(
                            "status", HttpStatus.OK.value(),
                            "message", "Medicamento registrado exitosamente",
                            "idMedicamento", id
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", HttpStatus.BAD_REQUEST.value(),
                            "error", "Datos inválidos",
                            "message", e.getMessage()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Error al registrar medicamento",
                            "message", e.getMessage()
                    )
            );
        }
    }
    @DeleteMapping("/eliminar/{idMedicamento}")
    public ResponseEntity<?> eliminarMedicamento(@PathVariable Integer idMedicamento) {
        try {
            service.eliminarMedicamentoPorId(idMedicamento);
            return ResponseEntity.ok(Map.of(
                    "status", HttpStatus.OK.value(),
                    "message", "Medicamento eliminado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "error", "Error al eliminar medicamento",
                    "message", e.getMessage()
            ));
        }
    }

}
