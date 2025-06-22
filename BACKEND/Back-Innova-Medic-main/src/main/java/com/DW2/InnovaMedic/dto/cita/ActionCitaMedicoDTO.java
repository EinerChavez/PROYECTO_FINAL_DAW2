package com.DW2.InnovaMedic.dto.cita;

import java.util.Arrays;
import java.util.List;

public record ActionCitaMedicoDTO(
        String notasMedicas,
        String diagnostico,
        String instruccionesAdicionales,
        String medicamentosText
) {
    public List<String> medicamentosList() {
        if (medicamentosText == null || medicamentosText.trim().isEmpty()) {
            return List.of();
        }

        return Arrays.stream(medicamentosText.split("\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> line.replaceFirst("^-\\s*", ""))
                .map(String::trim)
                .toList();
    }
}
