package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FilialRepository extends JpaRepository<Filial, Long> {

    @Query("select f from Filial f where trim(lower(f.nome)) = trim(lower(:nome))")
    Optional<Filial> findByNome(String nome);
}
