package Hospital_Belen.Proyecto_DAW2.repository;

import Hospital_Belen.Proyecto_DAW2.entity.DisponibilidadMedica;
import Hospital_Belen.Proyecto_DAW2.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface DisponibilidadMedicaRepository extends JpaRepository<DisponibilidadMedica, Integer> {
}