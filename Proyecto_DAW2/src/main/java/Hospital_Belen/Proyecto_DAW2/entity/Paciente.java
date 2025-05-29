package Hospital_Belen.Proyecto_DAW2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "PACIENTES")
@Data
public class Paciente {

    @Id
    @Column(name = "ID_PACIENTE")
    private Integer idPaciente;

    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "TALLA", nullable = false)
    private String talla;

    @Column(name = "GRUPO_SANGUINEO", nullable = false)
    private String grupoSanguineo;

    @Column(name = "DIRECCION", nullable = false)
    private String direccion;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ID_PACIENTE")
    private Usuario usuario;
}
