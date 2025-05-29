package Hospital_Belen.Proyecto_DAW2.repository;

import Hospital_Belen.Proyecto_DAW2.entity.Cita;
import Hospital_Belen.Proyecto_DAW2.entity.Medico;
import Hospital_Belen.Proyecto_DAW2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

}