package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RECETAS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA")
    private Integer idReceta;

    @OneToOne
    @JoinColumn(name = "ID_CITA", nullable = false, unique = true)
    private Cita cita;

    @Column(name = "INSTRUCCIONES_ADICIONALES", columnDefinition = "TEXT")
    private String instruccionesAdicionales;

    @Column(name = "FIRMA_MEDICO")
    private String firmaMedico;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicamentoReceta> medicamentos = new ArrayList<>();
}
