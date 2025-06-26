package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.DisponibilidadMedica;
import com.DW2.InnovaMedic.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadMedicaRepository extends JpaRepository<DisponibilidadMedica, Integer> {
    List<DisponibilidadMedica> findByMedico_IdUsuario(Integer idMedico);
    @Query("SELECT d FROM DisponibilidadMedica d WHERE d.medico.id = :idMedico AND d.diaSemana = :diaSemana")
    List<DisponibilidadMedica> findByMedicoAndDiaSemana(
            @Param("idMedico") Integer idMedico,
            @Param("diaSemana") DisponibilidadMedica.DiaSemana diaSemana);
    List<DisponibilidadMedica> findByMedicoAndDiaSemana(Medico medico, DisponibilidadMedica.DiaSemana diaSemana);
}

