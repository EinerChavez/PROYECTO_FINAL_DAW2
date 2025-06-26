package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.ListaRecetaDTO;
import com.DW2.InnovaMedic.dto.cita.RecetaDTO;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.DW2.InnovaMedic.service.MaintenanceReceta;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recetas")
public class RecetaController {

    private final MaintenanceReceta recetaService;
    private final MaintenancePdfExportService pdfexportService;

    @GetMapping("/pdf/{idReceta}")
    public ResponseEntity<byte[]> exportarReceta(@PathVariable Integer idReceta) {
        try {
            ListaRecetaDTO receta = recetaService.obtenerPorId(idReceta).orElseThrow(() ->
                    new IllegalArgumentException("No se encontró la receta con ID: " + idReceta)
            );

            byte[] pdfBytes = pdfexportService.exportarRecetaComoPdf(receta);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=receta-" + idReceta + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<ListaRecetaDTO> recetas = recetaService.listarTodas();
            if (recetas.isEmpty()) {
                Map<String, String> mensaje = new HashMap<>();
                mensaje.put("mensaje", "No hay recetas registradas.");
                return new ResponseEntity<>(mensaje, HttpStatus.OK);
            }
            return ResponseEntity.ok(recetas);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Error al listar recetas: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return recetaService.obtenerPorId(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(Map.of("mensaje", "No se encontró la receta con ID: " + id), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Error al obtener la receta: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<?> listarPorIdPaciente(@PathVariable Integer idPaciente) {
        try {
            List<ListaRecetaDTO> recetas = recetaService.listarPorIdPaciente(idPaciente);
            if (recetas.isEmpty()) {
                return new ResponseEntity<>(Map.of("mensaje", "No hay recetas asociadas al paciente con ID: " + idPaciente), HttpStatus.OK);
            }
            return ResponseEntity.ok(recetas);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Error al listar recetas del paciente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPorId(@PathVariable Integer id) {
        try {
            boolean existe = recetaService.obtenerPorId(id).isPresent();
            if (!existe) {
                return new ResponseEntity<>(Map.of("mensaje", "No se encontró la receta con ID: " + id), HttpStatus.NOT_FOUND);
            }
            recetaService.eliminarPorId(id);
            return ResponseEntity.ok(Map.of("mensaje", "Receta eliminada correctamente."));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Error al eliminar la receta: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
