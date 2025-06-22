package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "DISPONIBILIDAD_MEDICA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DISPONIBILIDAD")
    private Integer idDisponibilidad;

    @ManyToOne
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    private Medico medico;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIA_SEMANA", nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "HORA_INICIO", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "HORA_FIN", nullable = false)
    private LocalTime horaFin;

    public enum DiaSemana {
        Lunes, Martes, Miércoles, Jueves, Viernes, Sábado, Domingo
    }
}
