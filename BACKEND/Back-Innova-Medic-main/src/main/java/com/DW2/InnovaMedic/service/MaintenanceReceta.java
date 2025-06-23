package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.ListaRecetaDTO;

import java.util.List;
import java.util.Optional;

public interface MaintenanceReceta {
    List<ListaRecetaDTO> listarTodas();
    Optional<ListaRecetaDTO> obtenerPorId(Integer idReceta);
    List<ListaRecetaDTO> listarPorIdPaciente(Integer idPaciente);
    void eliminarPorId(Integer idReceta);

}
