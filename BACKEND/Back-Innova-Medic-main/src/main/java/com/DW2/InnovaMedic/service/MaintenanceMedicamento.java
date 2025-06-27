package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.MedicamentoRecetaDTO;

import java.util.List;

public interface MaintenanceMedicamento {
    List<MedicamentoRecetaDTO> listarPorIdReceta(Integer idReceta) throws Exception;
    Integer registrarMedicamento(MedicamentoRecetaDTO medicamentoRecetaDTO) throws Exception;
    void eliminarMedicamentoPorId(Integer idMedicamento) throws Exception;

}
