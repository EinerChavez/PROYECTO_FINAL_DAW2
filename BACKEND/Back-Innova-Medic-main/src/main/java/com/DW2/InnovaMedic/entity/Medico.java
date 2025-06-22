package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDICOS")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medico extends Usuario {
    @Column(name = "ESPECIALIDAD", nullable = false)
    private String especialidad;

    @Column(name = "NUMERO_COLEGIADO", nullable = false)
    private String numeroColegiado;

    @Column(name = "CODIGO_MEDICO_HOSPITAL", nullable = false, unique = true)
    private String codigoHospital;
}
