package Hospital_Belen.Proyecto_DAW2.dto;

import Hospital_Belen.Proyecto_DAW2.entity.Cita;

import java.time.LocalDateTime;

public class CitaDTO {

    public Integer idMedico;
    public Integer idPaciente;
    public LocalDateTime fechaHora;
    public String motivo;
    public Cita.Estado estado;
}
