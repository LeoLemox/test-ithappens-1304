package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}
