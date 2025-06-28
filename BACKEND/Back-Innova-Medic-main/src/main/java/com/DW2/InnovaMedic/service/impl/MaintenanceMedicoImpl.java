package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoResumenDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.DisponibilidadMedica;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.DisponibilidadMedicaRepository;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceMedicoImpl implements MaintenanceMedico {
    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final CitaRepository citaRepository;
    private final PasswordEncoder passwordEncoder;
    private final DisponibilidadMedicaRepository disponibilidamedicarepository;

    @Override
    public void registrarMedicos(MedicoRegistroDTO medicoRegistroDTO) throws Exception {
        usuarioRepository.findOneByEmail(medicoRegistroDTO.email())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + medicoRegistroDTO.email());
                });

        Medico medico = new Medico();
        medico.setNombre(medicoRegistroDTO.nombre());
        medico.setApellido(medicoRegistroDTO.apellido());
        medico.setSexo(medicoRegistroDTO.sexo());
        medico.setTelefono(medicoRegistroDTO.telefono());
        medico.setEmail(medicoRegistroDTO.email());
        medico.setContrasenia(passwordEncoder.encode(medicoRegistroDTO.contrasenia()));
        medico.setEspecialidad(medicoRegistroDTO.especialidad());
        medico.setNumeroColegiado(medicoRegistroDTO.numeroColegiado());
        medico.setCodigoHospital(medicoRegistroDTO.codigoHospital());

        medicoRepository.save(medico);
        registrarDisponibilidadPorDefecto(medico.getIdUsuario());
    }
    @Override
    public List<CitaDTO> obtenerCitasMedico(Integer id) throws Exception {
        if (!medicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Medico con Id " + id + " no existe");
        }

        List<Cita> citas = citaRepository.findByMedicoWithRecetasAndMedicamentos(id);

        return citas.stream()
                .map(cita -> CitaDTO.fromEntity(cita, cita.getReceta()))
                .toList();
    }

    @Override
    @Cacheable(value = "listaMedicos")
    public List<MedicoResumenDTO> listarMedicosResumen() throws Exception {
        List<Medico> medicos = medicoRepository.findAll();
        return medicos.stream()
                .map(MedicoResumenDTO::fromEntity)
                .toList();
    }


    @Transactional
    public void registrarDisponibilidadPorDefecto(Integer idMedico) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con ID: " + idMedico));

        List<DisponibilidadMedica> disponibilidad = new ArrayList<>();

        List<DisponibilidadMedica.DiaSemana> diasHabiles = List.of(
                DisponibilidadMedica.DiaSemana.Lunes,
                DisponibilidadMedica.DiaSemana.Martes,
                DisponibilidadMedica.DiaSemana.Miércoles,
                DisponibilidadMedica.DiaSemana.Jueves,
                DisponibilidadMedica.DiaSemana.Viernes
        );

        for (DisponibilidadMedica.DiaSemana dia : diasHabiles) {
            disponibilidad.add(new DisponibilidadMedica(
                    null,
                    medico,
                    dia,
                    LocalTime.of(8, 0),
                    LocalTime.of(12, 0))
            );

            disponibilidad.add(new DisponibilidadMedica(
                    null,
                    medico,
                    dia,
                    LocalTime.of(14, 0),
                    LocalTime.of(18, 0))
            );
        }

        disponibilidad.add(new DisponibilidadMedica(
                null,
                medico,
                DisponibilidadMedica.DiaSemana.Sábado,
                LocalTime.of(8, 0),
                LocalTime.of(12, 0))
        );
        disponibilidamedicarepository.saveAll(disponibilidad);
    }
    @Override
    public Map<String, List<String>> obtenerHorariosDisponiblesYOcupados(Integer idMedico, LocalDate fecha) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado"));

        DisponibilidadMedica.DiaSemana diaSemana = convertirDayOfWeekADiaSemana(fecha.getDayOfWeek());
        List<DisponibilidadMedica> disponibilidades = disponibilidamedicarepository
                .findByMedicoAndDiaSemana(medico, diaSemana);

        if (disponibilidades.isEmpty()) {
            return Map.of(
                    "disponibles", Collections.emptyList(),
                    "ocupados", Collections.emptyList()
            );
        }
        List<LocalTime> horasOcupadas = citaRepository.findHorasOcupadas(idMedico, fecha);
        Set<LocalTime> todosHorarios = new TreeSet<>();

        for (DisponibilidadMedica disp : disponibilidades) {
            LocalTime hora = disp.getHoraInicio();
            while (!hora.isAfter(disp.getHoraFin().minusHours(1))) {
                todosHorarios.add(hora);
                hora = hora.plusHours(1);
            }
            if (!hora.isAfter(disp.getHoraFin())) {
                todosHorarios.add(hora);
            }
        }
        List<String> disponibles = new ArrayList<>();
        List<String> ocupados = new ArrayList<>();

        for (LocalTime hora : todosHorarios) {
            if (horasOcupadas.contains(hora)) {
                ocupados.add(hora.toString());
            } else {
                disponibles.add(hora.toString());
            }
        }

        return Map.of(
                "disponibles", disponibles,
                "ocupados", ocupados
        );
    }

    private DisponibilidadMedica.DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DisponibilidadMedica.DiaSemana.Lunes;
            case TUESDAY -> DisponibilidadMedica.DiaSemana.Martes;
            case WEDNESDAY -> DisponibilidadMedica.DiaSemana.Miércoles;
            case THURSDAY -> DisponibilidadMedica.DiaSemana.Jueves;
            case FRIDAY -> DisponibilidadMedica.DiaSemana.Viernes;
            case SATURDAY -> DisponibilidadMedica.DiaSemana.Sábado;
            case SUNDAY -> DisponibilidadMedica.DiaSemana.Domingo;
        };
    }
}
