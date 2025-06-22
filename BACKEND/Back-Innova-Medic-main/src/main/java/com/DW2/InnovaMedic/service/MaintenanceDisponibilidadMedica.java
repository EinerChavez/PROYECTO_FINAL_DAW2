package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.slot.SlotDTO;
import com.DW2.InnovaMedic.dto.slot.SlotRequestDTO;

import java.util.List;

public interface MaintenanceDisponibilidadMedica {
    List<SlotDTO> obtenerSlotsDisponibles(SlotRequestDTO slotRequestDTO) throws Exception;
}
