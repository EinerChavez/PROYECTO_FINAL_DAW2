package Hospital_Belen.Proyecto_DAW2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "RECETAS")
@Data
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA")
    private Integer idReceta;

    @OneToOne
    @JoinColumn(name = "ID_CITA", nullable = false)
    private Cita cita;

    @Column(name = "CONTENIDO", nullable = false)
    private String contenido;

    @Column(name = "FECHA_EMISION", nullable = false)
    private LocalDateTime fechaEmision;
}