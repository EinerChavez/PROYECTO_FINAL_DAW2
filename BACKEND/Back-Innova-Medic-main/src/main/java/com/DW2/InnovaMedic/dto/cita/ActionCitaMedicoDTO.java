package com.DW2.InnovaMedic.dto.cita;

import java.util.List;

public record ActionCitaMedicoDTO(
        String notasMedicas,
        String diagnostico,
        String instruccionesAdicionales,
        List<MedicamentoRecetaRequestDTO> medicamentos
) {}
