package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.dto.slot.SlotDTO;
import com.DW2.InnovaMedic.dto.slot.SlotRequestDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import com.DW2.InnovaMedic.service.MaintenanceDisponibilidadMedica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cita")
public class CitaController {
    @Autowired
    MaintenanceCita maintenanceCita;

    @Autowired
    MaintenanceDisponibilidadMedica maintenanceDisponibilidadMedica;



    @GetMapping("/disponibilidad")
    public ResponseEntity<?> obtenerSlotsDisponibles(
            @RequestParam("idMedico") Integer idMedico,
            @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam("fechaFin") String fechaFinStr) {
        try {
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);

            SlotRequestDTO slotRequestDTO = new SlotRequestDTO(idMedico, fechaInicio, fechaFin);

            List<SlotDTO> slots = maintenanceDisponibilidadMedica.obtenerSlotsDisponibles(slotRequestDTO);

            if (slots.isEmpty()) {
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", HttpStatus.OK.value(),
                                "message", "No existen slots disponibles"
                        )
                );
            }

            return ResponseEntity.ok(slots);

        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", HttpStatus.NOT_FOUND.value(),
                            "error", "Problemas encontrados.",
                            "message", ie.getMessage()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Error interno al buscar citas",
                            "message", "Error encontrado"
                    )
            );
        }
    }
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCitaVacia(@RequestBody RegistrarCitaDTO registrarcita) {
        try {
            Integer idCita = maintenanceCita.registrarCitaVacia(registrarcita);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of("idCita", idCita)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al registrar la cita"
            ));
        }
    }
    @PutMapping("/actualizarReceta")
    public ResponseEntity<?> actualizarReceta(@RequestBody CitaRecetaVaciaDTO dto) {
        try {
            maintenanceCita.actualizarRecetaPorCita(dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Receta actualizada correctamente"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al actualizar receta: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizarEstado/{id}")
    public ResponseEntity<?> actualizarCitaCompleta(@PathVariable Integer id) {
        try {
            String estadoActualizado = maintenanceCita.actualizarEstadoCita(
                    id,
                    Cita.Estado.Finalizada
            );
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of(
                            "message", "Datos actualizados y medicamentos agregados",
                            "estadoActualizacion", estadoActualizado
                    )
            ));
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Datos inválidos: " + ie.getMessage()
            ));
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "error",
                    "message", "No se pudo actualizar el estado: " + ise.getMessage()
            ));
        } catch (ResponseStatusException rse) {
            assert rse.getReason() != null;

            return ResponseEntity.status(rse.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", rse.getReason()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al procesar la solicitud: " + e.getMessage()
            ));
        }
    }
    /*@PutMapping("/actualizar/informacion")
    public ResponseEntity<?> actualizarCitaCompleta(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        try {
            maintenanceCita.actualizarCitaCompleta(
                    actualizarCitaCompletaDTO.id(),
                    actualizarCitaCompletaDTO.actionCitaMedicoDTO(),
                    actualizarCitaCompletaDTO.nombreMedico()
            );

            String estadoActualizado = maintenanceCita.actualizarEstadoCita(
                    actualizarCitaCompletaDTO.id(),
                    Cita.Estado.Finalizada
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of(
                            "message", "Datos actualizados y medicamentos agregados",
                            "estadoActualizacion", estadoActualizado
                    )
            ));
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Datos inválidos: " + ie.getMessage()
            ));
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "error",
                    "message", "No se pudo actualizar el estado: " + ise.getMessage()
            ));
        } catch (ResponseStatusException rse) {
            assert rse.getReason() != null;

            return ResponseEntity.status(rse.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", rse.getReason()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al procesar la solicitud: " + e.getMessage()
            ));
        }
    }

     */

    @PutMapping("/actualizar/info-cita")
    public ResponseEntity<?> actualizarInformacionCita(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        try {
            maintenanceCita.actualizarInformacionMedicaCita(actualizarCitaCompletaDTO.id(), actualizarCitaCompletaDTO.actionCitaMedicoDTO());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Datos actualizados"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al editar la informacion de la cita: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> listaCitaId(@PathVariable Integer id) {
        try{
            CitaDTO citaDTO = maintenanceCita.obtenerCitas(id);

            if (citaDTO == null){
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", HttpStatus.OK.value(),
                                "message", "La cita no existe o aun no ha sido registrada",
                                "idCita", id
                        )
                );
            }
            return ResponseEntity.ok(citaDTO);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", HttpStatus.NOT_FOUND.value(),
                            "error", "Cita no encontrada",
                            "message", e.getMessage(),
                            "idSolicitado", id
                    )
            );
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Error interno al buscar citas",
                            "message", "Error de búsqueda con código " + id + ": " + e.getMessage()
                    )
            );
        }
    }

}
