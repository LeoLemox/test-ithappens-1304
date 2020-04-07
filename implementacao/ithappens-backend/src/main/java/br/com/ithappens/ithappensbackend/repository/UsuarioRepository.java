package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
