package Hospital_Belen.Proyecto_DAW2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "USUARIOS")
@Data
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

    @Enumerated(EnumType.STRING)
    @Column(name = "ROL", nullable = false)
    private Rol rol;

    public enum Sexo {
        Masculino, Femenino, Otro
    }

    public enum Rol {
        Paciente, Medico
    }
}
