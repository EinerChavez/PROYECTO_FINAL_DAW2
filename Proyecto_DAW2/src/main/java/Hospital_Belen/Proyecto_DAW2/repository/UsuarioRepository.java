package Hospital_Belen.Proyecto_DAW2.repository;

import Hospital_Belen.Proyecto_DAW2.entity.Usuario;
import Hospital_Belen.Proyecto_DAW2.entity.UsuarioCompleto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioCompleto, Integer> {
    Optional<Usuario> findByEmail(String email);

}
