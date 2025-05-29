package Hospital_Belen.Proyecto_DAW2.repository;

import Hospital_Belen.Proyecto_DAW2.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
}