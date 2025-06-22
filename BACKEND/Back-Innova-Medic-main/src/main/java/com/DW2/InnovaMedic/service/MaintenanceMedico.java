package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoResumenDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;

import java.util.List;

public interface MaintenanceMedico {
    void registrarMedicos(MedicoRegistroDTO medicoRegistroDTO) throws Exception;
    List<CitaDTO> obtenerCitasMedico(Integer id) throws Exception;
    List<MedicoResumenDTO> listarMedicosResumen() throws Exception; // Nuevo método
}