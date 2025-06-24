package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDICAMENTOS_RECETA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentoReceta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO")
    private Integer idMedicamento;

    @ManyToOne
    @JoinColumn(name = "ID_RECETA", nullable = false)
    private Receta receta;

    @Column(name = "MEDICAMENTO", length = 100, nullable = false)
    private String medicamento;

    @Column(name = "DOSIS", length = 50, nullable = false)
    private String dosis;

    @Column(name = "FRECUENCIA", length = 50, nullable = false)
    private String frecuencia;

    @Column(name = "DURACION", length = 50, nullable = false)
    private String duracion;

    @Column(name = "OBSERVACIONES", columnDefinition = "TEXT")
    private String observaciones;
}
