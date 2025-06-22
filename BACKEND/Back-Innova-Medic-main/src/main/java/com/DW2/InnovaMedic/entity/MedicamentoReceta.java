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

    @Column(name = "MEDICAMENTO", columnDefinition = "TEXT")
    private String medicamento;
}
