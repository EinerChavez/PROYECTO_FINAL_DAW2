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

@Service
@AllArgsConstructor
@Transactional
public class MaintenanceMedicamentoImpl implements MaintenanceMedicamento {

    private final MedicamentoRecetaRepository repoMedicamento;
    private final RecetaRepository repoReceta;
    private final CitaRepository repoCita;

    @Override
    public List<MedicamentoRecetaDTO> listarMedicamentos(Integer idPaciente, Integer idMedico) {
        List<Cita> citas = repoCita.findByPacienteAndMedicoWithRecetasAndMedicamentos(idPaciente, idMedico);

        return citas.stream()
                .flatMap(cita -> {
                    if (cita.getReceta() != null && cita.getReceta().getMedicamentos() != null) {
                        return cita.getReceta().getMedicamentos().stream()
                                .map(med -> new MedicamentoRecetaDTO(
                                        med.getIdMedicamento(), med.getReceta().getIdReceta(), med.getMedicamento(), med.getDosis(), med.getFrecuencia(), med.getDuracion(), med.getObservaciones()
                                ));
                    }
                    return null;
                })
                .filter(medDTO -> medDTO != null)
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
}
