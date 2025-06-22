package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.Receta;

import java.time.LocalDate;
import java.util.List;

public record RecetaDTO(
        Integer idReceta,
        String instruccionesAdicionales,
        String firmaMedico,
        LocalDate fecha,
        List<MedicamentoRecetaDTO> medicamentos
) {
    public static RecetaDTO fromEntity(Receta receta) {
        return new RecetaDTO(
                receta.getIdReceta(),
                receta.getInstruccionesAdicionales(),
                receta.getFirmaMedico(),
                receta.getFecha(),
                receta.getMedicamentos() == null
                        ? List.of()
                        : receta.getMedicamentos().stream()
                            .map(MedicamentoRecetaDTO::fromEntity)
                            .toList()
        );
    }
}
