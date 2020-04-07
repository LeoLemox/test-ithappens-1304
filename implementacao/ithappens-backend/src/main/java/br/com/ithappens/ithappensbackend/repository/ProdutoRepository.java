package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
