package Hospital_Belen.Proyecto_DAW2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MEDICOS")
@Data
public class Medico {

    @Id
    @Column(name = "ID_MEDICO")
    private Integer idMedico;

    @Column(name = "ESPECIALIDAD", nullable = false)
    private String especialidad;

    @Column(name = "NUMERO_COLEGIADO", nullable = false)
    private String numeroColegiado;

    @Column(name = "CODIGO_MEDICO_HOSPITAL", nullable = false, unique = true)
    private String codigoHospital;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ID_MEDICO")
    private Usuario usuario;
}
