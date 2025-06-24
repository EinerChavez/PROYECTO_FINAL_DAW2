package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.ListaRecetaDTO;

import java.util.List;
import java.util.Optional;

public interface MaintenanceReceta {
    List<ListaRecetaDTO> listarTodas() throws Exception;
    Optional<ListaRecetaDTO> obtenerPorId(Integer idReceta) throws Exception;
    List<ListaRecetaDTO> listarPorIdPaciente(Integer idPaciente) throws Exception;
    void eliminarPorId(Integer idReceta)throws Exception;

}
