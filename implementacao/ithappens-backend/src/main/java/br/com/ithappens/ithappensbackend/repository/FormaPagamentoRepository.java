package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

    @Query("select fp from FormaPagamento fp where trim(lower(fp.descricao)) = trim(lower(:descricao))")
    Optional<FormaPagamento> findByDescricao(String descricao);
}
