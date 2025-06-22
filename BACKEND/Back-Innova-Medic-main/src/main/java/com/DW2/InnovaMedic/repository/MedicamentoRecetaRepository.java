package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.MedicamentoReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRecetaRepository extends JpaRepository<MedicamentoReceta, Integer> {
    void deleteByReceta_IdReceta (Integer id);

}
