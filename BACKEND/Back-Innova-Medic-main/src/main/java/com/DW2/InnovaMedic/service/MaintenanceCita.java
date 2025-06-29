package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.entity.Cita;

public interface MaintenanceCita {
    Integer registrarCitaVacia(RegistrarCitaDTO registrarcita) throws Exception;
    Integer registrarRecetaPorCita(CitaRecetaVaciaDTO dto) throws Exception;
    String actualizarEstadoCita(Integer idCita, Cita.Estado nuevoEstado) throws Exception;
    //void actualizarCitaCompleta (Integer idCita, ActionCitaMedicoDTO request, String nombreMedico) throws Exception;
    CitaDTO obtenerCitas(Integer id) throws  Exception;
    void actualizarInformacionMedicaCita(Integer idCita, ActionCitaMedicoDTO request) throws Exception;
}
