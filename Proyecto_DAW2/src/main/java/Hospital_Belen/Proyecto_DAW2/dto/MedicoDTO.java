package Hospital_Belen.Proyecto_DAW2.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDTO {
    public String nombre;
    public String apellido;
    public String sexo;
    public String telefono;
    public String email;
    public String contrasenia;
    public String especialidad;
    public String numeroColegiado;
    public String codigoHospital;
}
