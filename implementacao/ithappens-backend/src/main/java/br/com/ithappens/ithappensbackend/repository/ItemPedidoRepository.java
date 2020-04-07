package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
