package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.MedicamentoReceta;

public record MedicamentoRecetaDTO(
        Integer idMedicamento,
        String medicamento
) {
    public static MedicamentoRecetaDTO fromEntity (MedicamentoReceta medicamentoReceta) {
        return new MedicamentoRecetaDTO(
                medicamentoReceta.getIdMedicamento(),
                medicamentoReceta.getMedicamento()
        );
    }
}
