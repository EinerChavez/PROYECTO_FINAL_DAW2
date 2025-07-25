package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.*;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    public void actualizarRecetaPorCita(CitaRecetaVaciaDTO dto) throws Exception {
        Receta receta = recetaRepository.findByCita_IdCitas(dto.idCita())
                .orElseThrow(() -> new IllegalArgumentException("No existe receta para esta cita"));
        receta.setInstruccionesAdicionales(dto.instruccionesAdicionales());
        receta.setFirmaMedico(dto.firmaMedico());

        recetaRepository.save(receta);
    }
    //@CacheEvict(value = {"citasPaciente", "citasMedico", "slotsDisponibles", "citasById"}, allEntries = true)
    public Integer registrarCitaVacia(RegistrarCitaDTO registrarcita) throws Exception {
        if (registrarcita.fecha() == null || registrarcita.hora() == null) {
            throw new IllegalArgumentException("Fecha y hora son requeridos");
        }

        Medico medico = medicoRepository.findById(registrarcita.idMedico())
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        Paciente paciente = pacienteRepository.findById(registrarcita.idPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        Cita cita = new Cita();
        cita.setMedico(medico);
        cita.setPaciente(paciente);
        cita.setFecha(registrarcita.fecha());
        cita.setHora(registrarcita.hora());
        cita.setTratamiento("aun no detallado");
        cita.setNotasMedicas("aun no detallado");
        cita.setDiagnostico("aun no detallado");
        cita.setEstado(Cita.Estado.Pendiente);

        Cita citaGuardada = citaRepository.save(cita);

        Receta receta = new Receta();
        receta.setCita(citaGuardada);
        receta.setFecha(registrarcita.fecha());
        receta.setInstruccionesAdicionales("aun no detallado");
        receta.setFirmaMedico("aun no detallado");

        recetaRepository.save(receta);

        return citaGuardada.getIdCitas();
    }

    @Override
    //@CacheEvict(value = {"citasPaciente", "citasMedico, slotsDisponibles, citasById"}, allEntries = true)
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

    private void actualizarInformacionCita(Integer idCita, String tratamiento, String notasMedicas, String diagnostico, LocalDate fecha, LocalTime hora) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cita " + idCita + " no existe"));

        cita.setTratamiento(tratamiento);
        cita.setNotasMedicas(notasMedicas);
        cita.setDiagnostico(diagnostico);
        cita.setFecha(fecha);
        cita.setHora(hora);
        citaRepository.save(cita);
    }

    private void actualizarReceta(Integer idCita, String instruccionesAdicionales, String firmaMedico) {
        Receta receta = recetaRepository.findByCita_IdCitas(idCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La receta " + idCita + " no existe"));

        receta.setInstruccionesAdicionales(instruccionesAdicionales);
        receta.setFirmaMedico(firmaMedico);
        if (firmaMedico != null) {
            receta.setFirmaMedico(firmaMedico);
        }
        recetaRepository.save(receta);
    }

    private void medicamentosReceta(Integer idCita, List<MedicamentoRecetaRequestDTO> listaMedicamentos) {
        if (listaMedicamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes proporcionar los medicamentos");
        }
        Receta receta = recetaRepository.findByCita_IdCitas(idCita)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Receta no encontrada para la cita con ID: " + idCita
                ));

        List<MedicamentoReceta> medicamentosActuales = medicamentoRecetaRepository.deleteByReceta_IdReceta(receta.getIdReceta());

        for (MedicamentoRecetaRequestDTO nuevo : listaMedicamentos) {
            boolean yaExiste = medicamentosActuales.stream().anyMatch(actual ->
                    actual.getMedicamento().equalsIgnoreCase(nuevo.medicamento().trim()) &&
                            actual.getDosis().equalsIgnoreCase(nuevo.dosis().trim()) &&
                            actual.getFrecuencia().equalsIgnoreCase(nuevo.frecuencia().trim()) &&
                            actual.getDuracion().equalsIgnoreCase(nuevo.duracion().trim()) &&
                            actual.getObservaciones().equalsIgnoreCase(nuevo.Observaciones().trim())
            );

            if (!yaExiste){
                MedicamentoReceta mr = new MedicamentoReceta();
                mr.setReceta(receta);
                mr.setMedicamento(nuevo.medicamento().trim());
                mr.setDosis(nuevo.dosis().trim());
                mr.setFrecuencia(nuevo.frecuencia().trim());
                mr.setDuracion(nuevo.duracion().trim());
                mr.setObservaciones(nuevo.Observaciones().trim());
                medicamentoRecetaRepository.save(mr);
            }
            }
    }


   /* @Override
    @CacheEvict(value = {"citasPaciente", "citasMedico, slotsDisponibles, citasById"}, allEntries = true)
    public void actualizarCitaCompleta(Integer idCita, ActionCitaMedicoDTO actionCitaMedicoDTO, String nombreMedico) {
        actualizarInformacionCita(idCita, actionCitaMedicoDTO.notasMedicas(), actionCitaMedicoDTO.diagnostico());

        actualizarReceta(idCita, actionCitaMedicoDTO.instruccionesAdicionales(), nombreMedico);

        medicamentosReceta(idCita, actionCitaMedicoDTO.medicamentos());
    }
*/
    @Override
    //@Cacheable(value = "citasById")
    public CitaDTO obtenerCitas(Integer id) throws Exception {
        if (!citaRepository.existsById(id)){
            throw new IllegalArgumentException("Cita con Id: " + id + " no existe");
        }

        Cita citas = citaRepository.findCitaByIdWithRecetaAndMedicamentos(id);
        return CitaDTO.fromEntity(citas, citas.getReceta());
    }

    @Override
   // @CacheEvict(value = {"citasPaciente", "citasMedico"}, allEntries = true)
    public void actualizarInformacionMedicaCita(Integer idCita, ActionCitaMedicoDTO request) throws Exception {
        actualizarInformacionCita(idCita, request.tratamiento(), request.notasMedicas(), request.diagnostico(), request.fecha(), request.hora());
    }
}
