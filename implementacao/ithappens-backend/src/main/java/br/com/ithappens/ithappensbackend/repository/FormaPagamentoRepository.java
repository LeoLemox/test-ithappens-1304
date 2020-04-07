package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
}
