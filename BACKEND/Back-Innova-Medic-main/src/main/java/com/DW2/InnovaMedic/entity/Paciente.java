package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "PACIENTES")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paciente extends Usuario {
    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "TALLA", nullable = false)
    private String talla;

    @Column(name = "GRUPO_SANGUINEO", nullable = false)
    private String grupoSanguineo;

    @Column(name = "DIRECCION", nullable = false)
    private String direccion;
}
