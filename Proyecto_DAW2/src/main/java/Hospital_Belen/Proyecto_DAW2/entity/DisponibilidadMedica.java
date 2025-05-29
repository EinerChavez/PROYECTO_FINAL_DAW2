package Hospital_Belen.Proyecto_DAW2.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "DISPONIBILIDAD_MEDICA")
@Data
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
    private DayOfWeek diaSemana;

    @Column(name = "HORA_INICIO", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "HORA_FIN", nullable = false)
    private LocalTime horaFin;
}