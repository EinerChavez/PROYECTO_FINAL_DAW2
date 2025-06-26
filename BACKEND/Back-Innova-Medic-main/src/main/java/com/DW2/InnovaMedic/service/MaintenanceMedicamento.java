package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.MedicamentoRecetaDTO;

import java.util.List;

public interface MaintenanceMedicamento {
    List<MedicamentoRecetaDTO> listarMedicamentos(Integer idPaciente, Integer idMedico) throws Exception;
    Integer registrarMedicamento(MedicamentoRecetaDTO medicamentoRecetaDTO) throws Exception;
}
