package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.MedicamentoRecetaDTO;
import com.DW2.InnovaMedic.dto.cita.RecetaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.MedicamentoReceta;
import com.DW2.InnovaMedic.entity.Receta;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.MedicamentoRecetaRepository;
import com.DW2.InnovaMedic.repository.RecetaRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedicamento;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MaintenanceMedicamentoImpl implements MaintenanceMedicamento {

    private final MedicamentoRecetaRepository repoMedicamento;
    private final RecetaRepository repoReceta;
    private final CitaRepository repoCita;

    @Override
    public List<MedicamentoRecetaDTO> listarPorIdReceta(Integer idReceta) {
        Optional<Receta> recetaOpt = repoReceta.findByIdWithMedicamentos(idReceta); // método personalizado en el repositorio

        if (recetaOpt.isEmpty() || recetaOpt.get().getMedicamentos() == null) {
            return List.of();
        }

        return recetaOpt.get().getMedicamentos().stream()
                .map(MedicamentoRecetaDTO::fromEntity)
                .toList();
    }


    @Override
    public Integer registrarMedicamento(MedicamentoRecetaDTO medicamentoRecetaDTO) throws Exception {

        Receta receta = repoReceta.findById(medicamentoRecetaDTO.idReceta())
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada con ID: " + medicamentoRecetaDTO.idReceta()));

        MedicamentoReceta nuevo = new MedicamentoReceta();
        nuevo.setMedicamento(medicamentoRecetaDTO.medicamento());
        nuevo.setDosis(medicamentoRecetaDTO.dosis());
        nuevo.setFrecuencia(medicamentoRecetaDTO.frecuencia());
        nuevo.setDuracion(medicamentoRecetaDTO.duracion());
        nuevo.setObservaciones(medicamentoRecetaDTO.observaciones());
        nuevo.setReceta(receta);

        return repoMedicamento.save(nuevo).getIdMedicamento();
    }
    @Override
    public void eliminarMedicamentoPorId(Integer idMedicamento) throws Exception {
        if (!repoMedicamento.existsById(idMedicamento)) {
            throw new IllegalArgumentException("El medicamento con ID " + idMedicamento + " no existe.");
        }
        repoMedicamento.deleteById(idMedicamento);
    }

}
