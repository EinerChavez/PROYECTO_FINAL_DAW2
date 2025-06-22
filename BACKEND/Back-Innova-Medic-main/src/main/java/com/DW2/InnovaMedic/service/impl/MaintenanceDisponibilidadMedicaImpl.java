package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.slot.SlotDTO;
import com.DW2.InnovaMedic.dto.slot.SlotRequestDTO;
import com.DW2.InnovaMedic.dto.projections.CitaHorarioProjection;
import com.DW2.InnovaMedic.entity.DisponibilidadMedica;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.DisponibilidadMedicaRepository;
import com.DW2.InnovaMedic.service.MaintenanceDisponibilidadMedica;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaintenanceDisponibilidadMedicaImpl implements MaintenanceDisponibilidadMedica {
    @Autowired
    DisponibilidadMedicaRepository disponibilidadMedicaRepository;

    @Autowired
    CitaRepository citaRepository;

    @Override
    @Cacheable(value = "slotsDisponibles", key = "#slotRequestDTO.idMedico() + '-' + #slotRequestDTO.fechaInicio() + '-' + #slotRequestDTO.fechaFin()")
    public List<SlotDTO> obtenerSlotsDisponibles(SlotRequestDTO slotRequestDTO) throws Exception {
        List<SlotDTO> slots = new ArrayList<>();

        List<DisponibilidadMedica> disponibilidadMedica = disponibilidadMedicaRepository
                .findByMedico_IdUsuario(slotRequestDTO.idMedico());

        Map<DisponibilidadMedica.DiaSemana, DisponibilidadMedica> disponibilidadPorDia = disponibilidadMedica.stream()
                .collect(Collectors.toMap(DisponibilidadMedica::getDiaSemana, d -> d));

        List<CitaHorarioProjection> citasActivas = citaRepository.obtenerCitasActivasEnRango(
                slotRequestDTO.idMedico(),
                slotRequestDTO.fechaInicio(),
                slotRequestDTO.fechaFin()
        );

        for (LocalDate fecha = slotRequestDTO.fechaInicio(); !fecha.isAfter(slotRequestDTO.fechaFin()); fecha = fecha.plusDays(1)) {
            DayOfWeek dayOfWeek = fecha.getDayOfWeek();
            String nombreDia = dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            nombreDia = nombreDia.substring(0, 1).toUpperCase() + nombreDia.substring(1).toLowerCase();

            DisponibilidadMedica.DiaSemana diaSemanaEnum;
            try {
                diaSemanaEnum = DisponibilidadMedica.DiaSemana.valueOf(nombreDia);
            } catch (IllegalArgumentException ilae) {
                continue;
            }

            if (disponibilidadPorDia.containsKey(diaSemanaEnum)) {
                DisponibilidadMedica disponibilidad = disponibilidadPorDia.get(diaSemanaEnum);
                LocalTime inicio = disponibilidad.getHoraInicio();
                LocalTime fin = disponibilidad.getHoraFin();

                while (!inicio.plusMinutes(30).isAfter(fin)) {
                    LocalTime slotInicio = inicio;
                    LocalTime slotFin = inicio.plusMinutes(30);
                    LocalDate slotFecha = fecha;

                    boolean ocupado = citasActivas.stream().anyMatch(cita -> {
                        if (!cita.getFecha().isEqual(slotFecha)) return false;

                        LocalTime citaInicio = cita.getHora();
                        LocalTime citaFin = citaInicio.plusHours(1);

                        return !(citaFin.isBefore(slotInicio) || citaInicio.isAfter(slotFin));
                    });

                    slots.add(new SlotDTO(
                            slotFecha,
                            nombreDia,
                            slotInicio.toString(),
                            slotFin.toString(),
                            !ocupado
                    ));

                    inicio = slotFin;
                }
            }
        }

        return slots;
    }
}
