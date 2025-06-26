package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.dto.projections.CitaHorarioProjection;
import com.DW2.InnovaMedic.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    @Query("""
            SELECT c FROM Cita c
                 LEFT JOIN FETCH c.receta r
                 LEFT JOIN FETCH r.medicamentos
                 WHERE c.paciente.idUsuario = :idPaciente
            """)
    List<Cita> findByPacienteWithRecetasAndMedicamentos(@Param("idPaciente") Integer idPaciente);

    @Query("""
             SELECT c FROM Cita c
             LEFT JOIN FETCH c.receta r
             LEFT JOIN FETCH r.medicamentos
             WHERE c.id = :idCita
             """)
    Cita findCitaByIdWithRecetaAndMedicamentos(@Param("idCita") Integer idCita);

    @Query("""
            SELECT c FROM Cita c
                LEFT JOIN FETCH c.receta r
                LEFT JOIN FETCH r.medicamentos m
            WHERE c.paciente.idUsuario = :idPaciente
              AND c.medico.idUsuario = :idMedico
          """)
    List<Cita> findByPacienteAndMedicoWithRecetasAndMedicamentos(@Param("idPaciente") Integer idPaciente,
                                                                 @Param("idMedico") Integer idMedico);
    @Query("""
            SELECT c FROM Cita c
                 LEFT JOIN FETCH c.receta r
                 LEFT JOIN FETCH r.medicamentos
                 WHERE c.medico.idUsuario = :idMedico
            """)
    List<Cita> findByMedicoWithRecetasAndMedicamentos(@Param("idMedico") Integer idMedico);

    @Query("""
                SELECT c.fecha AS fecha, c.hora AS hora
                FROM Cita c
                WHERE c.medico.idUsuario = :idMedico
                AND c.fecha BETWEEN :fechaInicio AND :fechaFin
                AND c.estado IN ('Pendiente', 'Confirmada')
            """)
    List<CitaHorarioProjection> obtenerCitasActivasEnRango(
            @Param("idMedico") Integer idMedico,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

}
