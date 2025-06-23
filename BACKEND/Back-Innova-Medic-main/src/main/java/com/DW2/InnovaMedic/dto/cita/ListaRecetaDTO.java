package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.MedicamentoReceta;
import com.DW2.InnovaMedic.entity.Receta;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record ListaRecetaDTO(
        Integer idReceta,
        LocalDate fecha,
        String nombrePaciente,
        String grupoSanguineoPaciente,
        String nombreMedico,
        String especialidadMedico,
        String diagnostico,
        String tratamiento,
        String instrucciones,
        String firmaMedico,
        List<String> medicamentos
) {
    public static ListaRecetaDTO from(Receta receta) {
        List<String> listaMedicamentos = receta.getMedicamentos()
                .stream()
                .map(MedicamentoReceta::getMedicamento)
                .collect(Collectors.toList());

        return new ListaRecetaDTO(
                receta.getIdReceta(),
                receta.getFecha(),
                receta.getCita().getPaciente().getNombre() + " " +
                        receta.getCita().getPaciente().getApellido(),
                receta.getCita().getPaciente().getGrupoSanguineo(),
                receta.getCita().getMedico().getNombre() + " " +
                        receta.getCita().getMedico().getApellido(),
                receta.getCita().getMedico().getEspecialidad(),
                receta.getCita().getDiagnostico(),
                receta.getCita().getTratamiento(),
                receta.getInstruccionesAdicionales(),
                receta.getFirmaMedico(),
                listaMedicamentos
        );
    }
}
