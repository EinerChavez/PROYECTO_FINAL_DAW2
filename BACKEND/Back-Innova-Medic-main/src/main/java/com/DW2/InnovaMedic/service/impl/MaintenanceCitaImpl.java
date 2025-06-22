package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.ActionCitaMedicoDTO;
import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.CitaRecetaVaciaDTO;
import com.DW2.InnovaMedic.entity.*;
import com.DW2.InnovaMedic.repository.*;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceCitaImpl implements MaintenanceCita {
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final RecetaRepository recetaRepository;
    private final MedicamentoRecetaRepository medicamentoRecetaRepository;

    @Override
    @CacheEvict(value = {"citasPaciente", "citasMedico, slotsDisponibles, citasById"}, allEntries = true)
    public Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO) throws Exception {
        if (citaRecetaVaciaDTO.fecha() == null || citaRecetaVaciaDTO.hora() == null) {
            throw new IllegalArgumentException("Fecha y hora son requeridos");
        }

        Medico medico = medicoRepository.findById(citaRecetaVaciaDTO.idMedico())
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        Paciente paciente = pacienteRepository.findById(citaRecetaVaciaDTO.idPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        Cita cita = new Cita();
        cita.setMedico(medico);
        cita.setPaciente(paciente);
        cita.setFecha(citaRecetaVaciaDTO.fecha());
        cita.setHora(citaRecetaVaciaDTO.hora());
        cita.setTratamiento(citaRecetaVaciaDTO.tratamiento());
        cita.setNotasMedicas("aun no detallado");
        cita.setDiagnostico("aun no detallado");
        cita.setEstado(Cita.Estado.Pendiente);

        Cita citaGuardada = citaRepository.save(cita);

        Receta receta = new Receta();
        receta.setCita(citaGuardada);
        receta.setFecha(citaRecetaVaciaDTO.fecha());
        receta.setInstruccionesAdicionales("aun no detallado");
        receta.setFirmaMedico("aun no detallado");

        recetaRepository.save(receta);

        return citaGuardada.getIdCitas();
    }

    @Override
    @CacheEvict(value = {"citasPaciente", "citasMedico, slotsDisponibles, citasById"}, allEntries = true)
    public String actualizarEstadoCita(Integer idCita, Cita.Estado nuevoEstado) throws Exception {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }

        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la cita con ID: " + idCita
                ));

        cita.setEstado(nuevoEstado);

        return "Estado de la cita actualizado correctamente a: " + nuevoEstado;
    }

    private void actualizarInformacionCita(Integer idCita, String notasMedicas, String diagnostico) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cita " + idCita + " no existe"));

        cita.setNotasMedicas(notasMedicas);
        cita.setDiagnostico(diagnostico);
        citaRepository.save(cita);
    }

    private void actualizarReceta(Integer idCita, String instruccionesAdicionales, String firmaMedico) {
        Receta receta = recetaRepository.findByCita_IdCitas(idCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La receta " + idCita + " no existe"));

        receta.setInstruccionesAdicionales(instruccionesAdicionales);
        receta.setFirmaMedico(firmaMedico);
        recetaRepository.save(receta);
    }

    private void medicamentosReceta(Integer idCita, List<String> listaMedicamentos) {
        Receta receta = recetaRepository.findByCita_IdCitas(idCita)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Receta no encontrada para la cita con ID: " + idCita
                ));

        medicamentoRecetaRepository.deleteByReceta_IdReceta(receta.getIdReceta());

        if (listaMedicamentos != null && !listaMedicamentos.isEmpty()) {
            List<MedicamentoReceta> medicamentos = listaMedicamentos.stream()
                    .filter(medicamento -> medicamento != null && !medicamento.trim().isEmpty())
                    .map(medicamento -> {
                        MedicamentoReceta mr = new MedicamentoReceta();
                        mr.setReceta(receta);
                        mr.setMedicamento(medicamento.trim());
                        return mr;
                    })
                    .collect(Collectors.toList());

            if (!medicamentos.isEmpty()) {
                medicamentoRecetaRepository.saveAll(medicamentos);
            }
        }
    }

    @Override
    @CacheEvict(value = {"citasPaciente", "citasMedico, slotsDisponibles, citasById"}, allEntries = true)
    public void actualizarCitaCompleta(Integer idCita, ActionCitaMedicoDTO request, String nombreMedico) {
        actualizarInformacionCita(idCita, request.notasMedicas(), request.diagnostico());

        actualizarReceta(idCita, request.notasMedicas(), nombreMedico);

        medicamentosReceta(idCita, request.medicamentosList());
    }

    @Override
    @Cacheable(value = "citasById")
    public CitaDTO obtenerCitas(Integer id) throws Exception {
        if (!citaRepository.existsById(id)){
            throw new IllegalArgumentException("Cita con Id: " + id + " no existe");
        }

        Cita citas = citaRepository.findCitaByIdWithRecetaAndMedicamentos(id);
        return CitaDTO.fromEntity(citas, citas.getReceta());
    }
}
