package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.DisponibilidadMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadMedicaRepository extends JpaRepository<DisponibilidadMedica, Integer> {
    List<DisponibilidadMedica> findByMedico_IdUsuario(Integer idMedico);
}
