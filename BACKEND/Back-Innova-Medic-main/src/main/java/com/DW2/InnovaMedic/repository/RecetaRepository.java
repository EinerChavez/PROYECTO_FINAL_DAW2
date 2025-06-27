package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    Optional<Receta> findByCita_IdCitas(Integer idCita);
    @Query("SELECT r FROM Receta r WHERE r.cita.paciente.id = :idPaciente")
    List<Receta> findByPacienteId(@Param("idPaciente") Integer idPaciente);
    @Query("SELECT r FROM Receta r LEFT JOIN FETCH r.medicamentos WHERE r.idReceta = :idReceta")
    Optional<Receta> findByIdWithMedicamentos(@Param("idReceta") Integer idReceta);

}
