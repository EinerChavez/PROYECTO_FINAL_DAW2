package Hospital_Belen.Proyecto_DAW2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vista_usuarios_completa")
@Data
public class UsuarioCompleto {

    @Id
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "SEXO")
    private String sexo;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTRASENIA")
    private String contrasenia;

    @Column(name = "ROL")
    private String rol;

    @Column(name = "FECHA_NACIMIENTO")
    private String fechaNacimiento;

    @Column(name = "TALLA")
    private String talla;

    @Column(name = "GRUPO_SANGUINEO")
    private String grupoSanguineo;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "ESPECIALIDAD")
    private String especialidad;

    @Column(name = "NUMERO_COLEGIADO")
    private String numeroColegiado;

    @Column(name = "CODIGO_MEDICO_HOSPITAL")
    private String codigoMedicoHospital;
}
