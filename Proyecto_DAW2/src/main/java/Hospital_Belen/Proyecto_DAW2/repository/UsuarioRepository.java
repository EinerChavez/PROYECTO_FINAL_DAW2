package Hospital_Belen.Proyecto_DAW2.repository;

import Hospital_Belen.Proyecto_DAW2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}