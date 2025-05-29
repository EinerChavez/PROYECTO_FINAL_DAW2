package Hospital_Belen.Proyecto_DAW2.repository;

import Hospital_Belen.Proyecto_DAW2.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {
}