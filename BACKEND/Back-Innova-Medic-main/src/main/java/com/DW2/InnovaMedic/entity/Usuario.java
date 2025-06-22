package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "USUARIOS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEXO", nullable = false)
    private Sexo sexo;

    @Column(name = "TELEFONO", nullable = false)
    private String telefono;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "CONTRASENIA", nullable = false)
    private String contrasenia;

    public enum Sexo {
        Masculino, Femenino, Otro
    }
}
