package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where lower(trim(u.email)) = lower(trim(:email)) ")
    Optional<Usuario> findByEmail(String email);
}
