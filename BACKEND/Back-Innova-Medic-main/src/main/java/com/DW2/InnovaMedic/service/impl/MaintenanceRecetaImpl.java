package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.ListaRecetaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Receta;
import com.DW2.InnovaMedic.repository.RecetaRepository;
import com.DW2.InnovaMedic.service.MaintenanceReceta;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceRecetaImpl implements MaintenanceReceta {

    private final RecetaRepository recetaRepository;

    public MaintenanceRecetaImpl(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @Override
    public List<ListaRecetaDTO> listarTodas() {
        return recetaRepository.findAll()
                .stream()
                .map(ListaRecetaDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ListaRecetaDTO> obtenerPorId(Integer idReceta) {
        return recetaRepository.findById(idReceta)
                .map(ListaRecetaDTO::from);
    }

    @Override
    public List<ListaRecetaDTO> listarPorIdPaciente(Integer idPaciente) {
        return recetaRepository.findByPacienteId(idPaciente)
                .stream()
                .map(ListaRecetaDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void eliminarPorId(Integer idReceta) {
        Receta receta = recetaRepository.findById(idReceta)
                .orElseThrow(() -> new RuntimeException("No se encontró la receta con ID: " + idReceta));
        Cita cita = receta.getCita();
        if (cita != null) {
            cita.setReceta(null);
        }
        receta.setCita(null);
        recetaRepository.delete(receta);
    }
}
