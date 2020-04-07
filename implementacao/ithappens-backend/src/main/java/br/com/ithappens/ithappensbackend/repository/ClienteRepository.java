package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
