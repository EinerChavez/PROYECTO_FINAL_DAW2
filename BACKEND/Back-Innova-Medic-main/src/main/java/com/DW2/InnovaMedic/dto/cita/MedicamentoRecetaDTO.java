package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.MedicamentoReceta;

public record MedicamentoRecetaDTO(
        Integer idMedicamento,
        Integer idReceta,
        String medicamento,
        String dosis,
        String frecuencia,
        String duracion,
        String observaciones
) {
    public static MedicamentoRecetaDTO fromEntity (MedicamentoReceta medicamentoReceta) {
        return new MedicamentoRecetaDTO(
                medicamentoReceta.getIdMedicamento(),
                medicamentoReceta.getReceta().getIdReceta(),
                medicamentoReceta.getMedicamento(),
                medicamentoReceta.getDosis(),
                medicamentoReceta.getFrecuencia(),
                medicamentoReceta.getDuracion(),
                medicamentoReceta.getObservaciones()
        );
    }
}
