package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.service.MaintenanceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    MaintenanceUsuario maintenanceUsuario;

    @GetMapping("/{id}/perfil")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        try {
            Object usuarioDTO = maintenanceUsuario.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "user", usuarioDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "error",
                    "message", "Error al obtener el usuario: " + e.getMessage()
            ));
        }
    }
}
