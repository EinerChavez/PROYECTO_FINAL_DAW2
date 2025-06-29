package com.DW2.InnovaMedic.dto.cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ActionCitaMedicoDTO(
        String tratamiento,
        String notasMedicas,
        String diagnostico,
        LocalDate fecha,
        LocalTime hora,
        List<MedicamentoRecetaRequestDTO> medicamentos
) {}
