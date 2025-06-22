package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findOneByEmail(String email);
}
