package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.ListaRecetaDTO;
import com.DW2.InnovaMedic.service.MaintenanceReceta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final MaintenanceReceta recetaService;

    public RecetaController(MaintenanceReceta recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        List<ListaRecetaDTO> recetas = recetaService.listarTodas();
        if (recetas.isEmpty()) {
            Map<String, String> mensaje = new HashMap<>();
            mensaje.put("mensaje", "No hay recetas registradas.");
            return new ResponseEntity<>(mensaje, HttpStatus.OK); // puedes usar 200 o 204
        }
        return ResponseEntity.ok(recetas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return recetaService.obtenerPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> mensaje = new HashMap<>();
                    mensaje.put("mensaje", "No se encontró la receta con ID: " + id);
                    return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<?> listarPorIdPaciente(@PathVariable Integer idPaciente) {
        List<ListaRecetaDTO> recetas = recetaService.listarPorIdPaciente(idPaciente);
        if (recetas.isEmpty()) {
            Map<String, String> mensaje = new HashMap<>();
            mensaje.put("mensaje", "No hay recetas asociadas al paciente con ID: " + idPaciente);
            return new ResponseEntity<>(mensaje, HttpStatus.OK); // también puede ser 204
        }
        return ResponseEntity.ok(recetas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPorId(@PathVariable Integer id) {
        boolean existe = recetaService.obtenerPorId(id).isPresent();
        if (!existe) {
            Map<String, String> mensaje = new HashMap<>();
            mensaje.put("mensaje", "No se encontró la receta con ID: " + id);
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
        recetaService.eliminarPorId(id);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Receta eliminada correctamente.");
        return ResponseEntity.ok(respuesta);
    }

}
