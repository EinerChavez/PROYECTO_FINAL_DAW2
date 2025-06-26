package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.MedicamentoReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentoRecetaRepository extends JpaRepository<MedicamentoReceta, Integer> {
    List<MedicamentoReceta> deleteByReceta_IdReceta (Integer id);

}
