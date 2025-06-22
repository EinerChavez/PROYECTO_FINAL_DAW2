package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.registro.PacienteRegistroDTO;

import java.util.List;

public interface MaintenancePaciente {
    void registrarPaciente(PacienteRegistroDTO pacienteRegistroDTO) throws Exception;
    List<CitaDTO> obtenerCitasPaciente(Integer id) throws Exception;
}
