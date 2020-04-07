package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
