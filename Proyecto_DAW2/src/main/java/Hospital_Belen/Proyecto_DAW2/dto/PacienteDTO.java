package Hospital_Belen.Proyecto_DAW2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    public String nombre;
    public String apellido;
    public String sexo;
    public String telefono;
    public String email;
    public String contrasenia;
    public LocalDate fechaNacimiento;
    public String talla;
    public String grupoSanguineo;
    public String direccion;
}


